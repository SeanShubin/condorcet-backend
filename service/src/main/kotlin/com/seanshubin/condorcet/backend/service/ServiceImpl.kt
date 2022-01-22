package com.seanshubin.condorcet.backend.service

import arrow.core.Either
import arrow.core.Either.Left
import arrow.core.Either.Right
import com.seanshubin.condorcet.backend.crypto.PasswordUtil
import com.seanshubin.condorcet.backend.crypto.UniqueIdGenerator
import com.seanshubin.condorcet.backend.database.*
import com.seanshubin.condorcet.backend.database.DbElectionUpdates.Companion.toDbElectionUpdates
import com.seanshubin.condorcet.backend.domain.*
import com.seanshubin.condorcet.backend.domain.Permission.*
import com.seanshubin.condorcet.backend.domain.Ranking.Companion.addMissingCandidates
import com.seanshubin.condorcet.backend.domain.Ranking.Companion.effectiveRankings
import com.seanshubin.condorcet.backend.domain.Ranking.Companion.voterBiasedOrdering
import com.seanshubin.condorcet.backend.domain.Role.Companion.SECONDARY_ROLE
import com.seanshubin.condorcet.backend.domain.Role.Companion.PRIMARY_ROLE
import com.seanshubin.condorcet.backend.domain.Role.Companion.DEFAULT_ROLE
import com.seanshubin.condorcet.backend.genericdb.GenericTable
import com.seanshubin.condorcet.backend.service.CaseInsensitiveStringListUtil.extra
import com.seanshubin.condorcet.backend.service.CaseInsensitiveStringListUtil.missing
import com.seanshubin.condorcet.backend.service.DataTransfer.toElectionDetail
import com.seanshubin.condorcet.backend.service.DataTransfer.toElectionSummary
import com.seanshubin.condorcet.backend.service.ServiceException.Category
import com.seanshubin.condorcet.backend.service.ServiceException.Category.*
import java.time.Clock
import java.time.Instant
import kotlin.random.Random

class ServiceImpl(
    private val passwordUtil: PasswordUtil,
    private val eventDbQueries: EventDbQueries,
    private val stateDbQueries: StateDbQueries,
    private val stateDbCommands: StateDbCommands,
    private val synchronizer: Synchronizer,
    private val random: Random,
    private val clock: Clock,
    private val uniqueIdGenerator: UniqueIdGenerator
) : Service {
    override fun synchronize() {
        synchronizer.synchronize()
    }

    override fun refresh(refreshToken: RefreshToken): Tokens {
        val user = findUser(refreshToken.userName)
        val accessToken = AccessToken(user.name, user.role)
        return Tokens(refreshToken, accessToken)
    }

    override fun register(userName: String, email: String, password: String): Tokens {
        val validName = validateUserName(userName)
        val validEmail = validateEmail(email)
        val validPassword = validatePassword(password)
        requireUserDoesNotExist(validName)
        requireEmailDoesNotExist(validEmail)
        val role = if (userCount() == 0) {
            PRIMARY_ROLE
        } else {
            DEFAULT_ROLE
        }
        createUser(validName, validEmail, validPassword, role)
        val user = findUser(validName)
        return createTokens(user)
    }

    override fun authenticate(nameOrEmail: String, password: String): Tokens {
        val user = findUserByNameOrEmail(nameOrEmail)
        requirePasswordMatches(nameOrEmail, password, user.salt, user.hash)
        return createTokens(user)
    }

    override fun permissionsForRole(role: Role): List<Permission> =
        stateDbQueries.listPermissions(role)

    override fun setRole(accessToken: AccessToken, userName: String, role: Role) {
        requirePermission(accessToken, MANAGE_USERS)
        requireCanChangeRole(accessToken.userName, userName, role)
        if (role == PRIMARY_ROLE) {
            stateDbCommands.setRole(accessToken.userName, accessToken.userName, SECONDARY_ROLE)
        }
        stateDbCommands.setRole(accessToken.userName, userName, role)
    }

    override fun removeUser(accessToken: AccessToken, userName: String) {
        requirePermission(accessToken, MANAGE_USERS)
        val user = findUser(userName)
        requireGreaterRole(accessToken, user)
        if(isSelf(accessToken, user) && userCount() > 1){
            fail(UNSUPPORTED,"Can not remove yourself unless you are the only user")
        }
        stateDbCommands.removeUser(accessToken.userName, userName)
    }

    override fun listUsers(accessToken: AccessToken): List<UserNameRole> {
        requirePermission(accessToken, MANAGE_USERS)
        val self = findUserRolePermissions(accessToken.userName)
        val user = stateDbQueries.listUsers()
        val list = user.map { row ->
            val userRole = UserRole(row.name, row.role)
            val allowedRoles = self.listedRolesFor(userRole)
            UserNameRole(row.name, row.role, allowedRoles)
        }
        return list
    }

    override fun addElection(accessToken: AccessToken, electionName: String) {
        requirePermission(accessToken, USE_APPLICATION)
        val validElectionName = validateElectionName(electionName)
        requireElectionNameDoesNotExist(electionName)
        stateDbCommands.addElection(accessToken.userName, accessToken.userName, validElectionName)
    }

    override fun launchElection(accessToken: AccessToken, electionName: String, allowEdit: Boolean) {
        requirePermission(accessToken, USE_APPLICATION)
        requireIsElectionOwner(accessToken, electionName)
        requireHasMoreThanOneCandidate(electionName)
        val updates = DbElectionUpdates(allowVote = true, allowEdit = allowEdit)
        stateDbCommands.updateElection(accessToken.userName, electionName, updates)
    }

    override fun finalizeElection(accessToken: AccessToken, electionName: String) {
        requirePermission(accessToken, USE_APPLICATION)
        val election = findElection(electionName)
        requireIsElectionOwner(accessToken, election)
        requireHasNoEndDate(election, "Only elections with no end date can be manually finalized")
        val updates = DbElectionUpdates(allowVote = false, allowEdit = false)
        stateDbCommands.updateElection(accessToken.userName, electionName, updates)
    }

    override fun updateElection(accessToken: AccessToken, electionName: String, electionUpdates: ElectionUpdates) {
        requirePermission(accessToken, USE_APPLICATION)
        val election = findElection(electionName)
        requireIsElectionOwner(accessToken, election)
        requireNotLaunched(election)
        val validElectionUpdates = validateElectionUpdates(electionUpdates)
        stateDbCommands.updateElection(accessToken.userName, electionName, validElectionUpdates)
    }

    override fun getElection(accessToken: AccessToken, electionName: String): ElectionDetail {
        requirePermission(accessToken, VIEW_APPLICATION)
        val election = findElection(electionName)
        val candidateCount = stateDbQueries.candidateCount(electionName)
        val voterCount = stateDbQueries.voterCount(electionName)
        val electionDetail = election.toElectionDetail(candidateCount, voterCount)
        return electionDetail
    }

    override fun deleteElection(accessToken: AccessToken, electionName: String) {
        requirePermission(accessToken, USE_APPLICATION)
        requireIsElectionOwner(accessToken, electionName)
        stateDbCommands.deleteElection(accessToken.userName, electionName)
    }

    override fun listElections(accessToken: AccessToken): List<ElectionSummary> {
        requirePermission(accessToken, VIEW_APPLICATION)
        val rows = stateDbQueries.listElections()
        val list = rows.map { it.toElectionSummary() }
        return list
    }

    override fun listTables(accessToken: AccessToken): List<String> {
        requirePermission(accessToken, VIEW_SECRETS)
        return stateDbQueries.tableNames(StateSchema)
    }

    override fun userCount(accessToken: AccessToken): Int {
        requirePermission(accessToken, VIEW_APPLICATION)
        return stateDbQueries.userCount()
    }

    override fun electionCount(accessToken: AccessToken): Int {
        requirePermission(accessToken, VIEW_APPLICATION)
        return stateDbQueries.electionCount()
    }

    override fun tableCount(accessToken: AccessToken): Int {
        requirePermission(accessToken, VIEW_SECRETS)
        return stateDbQueries.tableCount()
    }

    override fun eventCount(accessToken: AccessToken): Int {
        requirePermission(accessToken, VIEW_SECRETS)
        return eventDbQueries.eventCount()
    }

    override fun tableData(accessToken: AccessToken, tableName: String): TableData {
        requirePermission(accessToken, VIEW_SECRETS)
        val genericTable = stateDbQueries.tableData(StateSchema, tableName)
        return genericTable.toTableData()
    }

    override fun debugTableData(accessToken: AccessToken, tableName: String): TableData {
        requirePermission(accessToken, VIEW_SECRETS)
        val genericTable = stateDbQueries.debugTableData(StateSchema, tableName)
        return genericTable.toTableData()
    }

    override fun eventData(accessToken: AccessToken): TableData {
        requirePermission(accessToken, VIEW_SECRETS)
        val genericTable = eventDbQueries.tableData(EventSchema, "event")
        return genericTable.toTableData()
    }

    override fun setCandidates(accessToken: AccessToken, electionName: String, candidateNames: List<String>) {
        requirePermission(accessToken, USE_APPLICATION)
        val election = findElection(electionName)
        requireIsElectionOwner(accessToken, election)
        requireElectionCanEdit(election)
        val validCandidateNames = validateCandidateNames(candidateNames)
        val originalCandidates = validateCandidateNames(stateDbQueries.listCandidates(electionName))
        val candidatesToDelete = originalCandidates.missing(validCandidateNames)
        val candidatesToAdd = originalCandidates.extra(validCandidateNames)
        if (candidatesToDelete.isNotEmpty()) {
            stateDbCommands.removeCandidates(accessToken.userName, electionName, candidatesToDelete)
        }
        if (candidatesToAdd.isNotEmpty()) {
            stateDbCommands.addCandidates(accessToken.userName, electionName, candidatesToAdd)
        }
    }

    override fun listCandidates(accessToken: AccessToken, electionName: String): List<String> {
        requirePermission(accessToken, VIEW_APPLICATION)
        val candidateNames = stateDbQueries.listCandidates(electionName)
        return candidateNames
    }

    override fun castBallot(
        accessToken: AccessToken,
        voterName: String,
        electionName: String,
        rankings: List<Ranking>
    ) {
        requirePermission(accessToken, USE_APPLICATION)
        requireElectionIsAllowingVotes(electionName)
        requireIsUser(
            accessToken,
            voterName,
            "User '${accessToken.userName}' not allowed to cast a ballot on behalf of voter '$voterName'"
        )
        val ballotSummary = searchBallotSummary(voterName, electionName)
        if (ballotSummary == null) {
            castNewBallot(voterName, electionName, rankings)
        } else {
            val confirmation = ballotSummary.confirmation
            updateExistingBallot(voterName, electionName, confirmation, rankings)
        }
    }

    override fun listRankings(accessToken: AccessToken, voterName: String, electionName: String): List<Ranking> {
        requirePermission(accessToken, VIEW_APPLICATION)
        requireIsUser(
            accessToken,
            voterName,
            "User '${accessToken.userName}' not allowed to see ballot cast by voter '$voterName'"
        )
        val candidates = stateDbQueries.listCandidates(electionName)
        val rankings = stateDbQueries.listRankings(voterName, electionName)
        return rankings.addMissingCandidates(candidates).voterBiasedOrdering(random)
    }

    override fun tally(accessToken: AccessToken, electionName: String): Tally {
        requirePermission(accessToken, VIEW_APPLICATION)
        val now = clock.instant()
        requireTallyAvailable(electionName, now)
        val election = findElection(electionName)
        val secretBallot = election.secretBallot
        val candidates = stateDbQueries.listCandidates(electionName)
        val ballots = stateDbQueries.listBallots(electionName)
        val tally = Tally.countBallots(secretBallot, candidates, ballots)
        return tally
    }

    override fun listEligibility(accessToken: AccessToken, electionName: String): List<VoterEligibility> {
        requirePermission(accessToken, VIEW_APPLICATION)
        val allVoters = stateDbQueries.listVoterNames()
        val eligibleVoters = stateDbQueries.listVotersForElection(electionName)
        val list = allVoters.map {
            val eligible = eligibleVoters.contains(it)
            VoterEligibility(it, eligible)
        }
        return list
    }

    override fun setEligibleVoters(accessToken: AccessToken, electionName: String, voterNames: List<String>) {
        requirePermission(accessToken, USE_APPLICATION)
        val election = findElection(electionName)
        requireIsElectionOwner(accessToken, election)
        requireElectionCanEdit(election)
        validateVoterNames(voterNames)
        val originalVoters = stateDbQueries.listVotersForElection(electionName)
        val votersToDelete = originalVoters.missing(voterNames)
        val votersToAdd = originalVoters.extra(voterNames)
        if (votersToDelete.isNotEmpty()) {
            stateDbCommands.removeVoters(accessToken.userName, electionName, votersToDelete)
        }
        if (votersToAdd.isNotEmpty()) {
            stateDbCommands.addVoters(accessToken.userName, electionName, votersToAdd)
        }
    }

    override fun getBallot(accessToken: AccessToken, voterName: String, electionName: String): BallotSummary? {
        requirePermission(accessToken, VIEW_APPLICATION)
        val now = clock.instant()
        requireCanSeeBallot(accessToken, voterName, electionName, now)
        val ballotSummary = stateDbQueries.searchBallot(voterName, electionName)
        return ballotSummary
    }

    override fun isEligible(accessToken: AccessToken, userName: String, electionName: String): Boolean {
        requirePermission(accessToken, VIEW_APPLICATION)
        val eligibleVoters = stateDbQueries.listVotersForElection(electionName)
        return eligibleVoters.contains(userName)
    }

    private fun requireIsUser(accessToken: AccessToken, userName: String, message: String) {
        if (accessToken.userName != userName) {
            fail(UNAUTHORIZED, message)
        }
    }

    private fun findUserRolePermissions(userName: String): UserRolePermissions {
        val user = findUser(userName)
        val permissions = permissionsForRole(user.role)
        return UserRolePermissions(userName, user.role, permissions)
    }

    private fun findUser(userName: String): UserRow {
        return stateDbQueries.findUserByName(userName)
    }

    private fun userExists(name: String): Boolean = stateDbQueries.searchUserByName(name) != null
    private fun emailExists(email: String): Boolean = stateDbQueries.searchUserByEmail(email) != null
    private fun roleIsGreater(first: Role, second: Role): Boolean = first.ordinal > second.ordinal
    private fun hasPermission(role: Role, permission: Permission): Boolean =
        stateDbQueries.roleHasPermission(role, permission)

    private fun hasPermission(accessToken: AccessToken, permission: Permission): Boolean =
        hasPermission(accessToken.role, permission)

    private fun isSelf(accessToken: AccessToken, userRow: UserRow): Boolean = accessToken.userName == userRow.name

    private fun fail(category: ServiceException.Category, message: String): Nothing {
        throw ServiceException(category, message)
    }

    private fun failIf(shouldFail: Boolean, category: ServiceException.Category, message: String) {
        if (shouldFail) throw ServiceException(category, message)
    }

    private fun failUnless(shouldNotFail: Boolean, category: ServiceException.Category, message: String) {
        failIf(!shouldNotFail, category, message)
    }

    private fun requirePermission(accessToken: AccessToken, permission: Permission) {
        failUnless(
            hasPermission(accessToken, permission), UNAUTHORIZED,
            "User ${accessToken.userName} with role ${accessToken.role} does not have permission $permission"
        )

    }

    private fun searchBallotSummary(
        voterName: String,
        electionName: String
    ): BallotSummary? {
        return stateDbQueries.searchBallot(voterName, electionName)
    }

    private fun searchUserByName(name: String): UserRow? = stateDbQueries.searchUserByName(name)
    private fun searchUserByEmail(email: String): UserRow? = stateDbQueries.searchUserByEmail(email)
    private fun searchUserByNameOrEmail(nameOrEmail: String): UserRow? {
        val byName = searchUserByName(nameOrEmail)
        if (byName != null) return byName
        val byEmail = searchUserByEmail(nameOrEmail)
        return byEmail
    }

    private fun createTokens(userRow: UserRow): Tokens {
        val refreshToken = RefreshToken(userRow.name)
        val accessToken = AccessToken(userRow.name, userRow.role)
        return Tokens(refreshToken, accessToken)
    }

    private fun GenericTable.toTableData(): TableData {
        val columnNames = this.columnNames
        val rows = this.rows
        return TableData(columnNames, rows)
    }

    private fun validateString(original: String, caption: String, rule: (String) -> Either<String, String>): String =
        when (val validated = rule(original)) {
            is Right -> validated.value
            is Left -> fail(UNSUPPORTED, "Invalid $caption, ${validated.value}")
        }

    private fun validateStringList(
        original: List<String>,
        caption: String,
        rule: (String) -> Either<String, String>
    ): List<String> =
        original.filter { it.trim() != "" }.map { validateString(it, "$caption '$it'", rule) }

    private fun findElection(electionName: String): ElectionRow {
        val electionRow = stateDbQueries.searchElectionByName(electionName)
        failIf(electionRow == null, NOT_FOUND, "Election with name '$electionName' not found")
        return electionRow!!
    }

    private fun validateVoterNames(voterNames: List<String>) {
        val userNames = stateDbQueries.listUserNames()
        val invalidNames = voterNames.filterNot { userNames.contains(it) }
        if (invalidNames.isNotEmpty()) {
            val invalidNamesString = invalidNames.joinToString("', '", "'", "'")
            fail(UNSUPPORTED, "The following are not valid user names: $invalidNamesString")
        }
    }

    private fun castNewBallot(
        voterName: String,
        electionName: String,
        rankings: List<Ranking>
    ) {
        val effectiveRankings = rankings.effectiveRankings()
        val now = clock.instant()
        val confirmation = uniqueIdGenerator.uniqueId()
        stateDbCommands.castBallot(
            voterName,
            voterName,
            electionName,
            effectiveRankings,
            confirmation,
            now
        )
    }

    private fun updateExistingBallot(
        voterName: String,
        electionName: String,
        confirmation: String,
        rankings: List<Ranking>
    ) {
        val effectiveRankings = rankings.effectiveRankings()
        val now = clock.instant()
        stateDbCommands.setRankings(voterName, confirmation, electionName, effectiveRankings)
        stateDbCommands.updateWhenCast(voterName, confirmation, now)
    }

    private fun requireElectionIsAllowingVotes(electionName: String) {
        val election = findElection(electionName)
        if (!election.allowVote) {
            if (election.allowEdit) {
                fail(UNAUTHORIZED, "Election $electionName is not accepting votes yet")
            } else {
                fail(UNAUTHORIZED, "Election $electionName is no longer accepting votes")
            }
        }
    }

    private fun requireUserDoesNotExist(userName: String) {
        if (userExists(userName)) {
            fail(CONFLICT, "User with name '$userName' already exists")
        }
    }

    private fun requireEmailDoesNotExist(email: String) {
        if (emailExists(email)) {
            fail(CONFLICT, "User with email '$email' already exists")
        }
    }

    private fun createUser(userName: String, email: String, password: String, role: Role) {
        val (salt, hash) = passwordUtil.createSaltAndHash(password)
        stateDbCommands.createUser(userName, userName, email, salt, hash, role)
    }

    private fun validateUserName(userName:String):String =
        validateString(userName, "name", Validation.userName)
    private fun validateEmail(email:String):String =
        validateString(email, "email", Validation.email)
    private fun validatePassword(password:String):String =
        validateString(password, "password", Validation.password)

    private fun userCount():Int = stateDbQueries.userCount()

    private fun findUserByNameOrEmail(nameOrEmail:String):UserRow {
        return searchUserByNameOrEmail(nameOrEmail)
            ?: fail(NOT_FOUND, "User with name or email '$nameOrEmail' does not exist")
    }

    private fun requirePasswordMatches(nameOrEmail:String, password:String, salt:String, hash:String){
        if(!passwordUtil.passwordMatches(password, salt, hash)){
            fail(UNAUTHORIZED, "Authentication failed for user with name or email '$nameOrEmail'")
        }
    }

    private fun requireCanChangeRole(authorizingUserName:String, targetUserName:String, role:Role){
        val authorizingUserRolePermissions = findUserRolePermissions(authorizingUserName)
        val targetUser = findUser(targetUserName)
        val targetUserRole = UserRole(targetUser.name, targetUser.role)
        val canChangeRole = authorizingUserRolePermissions.canChangeRole(targetUserRole, role)
        canChangeRole.mapLeft { message ->
            fail(UNAUTHORIZED, message)
        }
    }

    private fun requireGreaterRole(accessToken: AccessToken, user:UserRow){
        if(accessToken.role <= user.role){
            fail(UNAUTHORIZED, "${accessToken.userName} with role ${accessToken.role} does not have greater role than ${user.name} with role ${user.role}")
        }
    }

    private fun validateElectionName(electionName:String):String =
        validateString(electionName, "name", Validation.electionName)

    private fun searchElection(electionName:String):ElectionRow? =
        stateDbQueries.searchElectionByName(electionName)

    private fun requireElectionNameDoesNotExist(electionName:String){
        val election = searchElection(electionName)
        if(election != null){
            fail(CONFLICT, "Election named $electionName already exists")
        }
    }

    private fun requireIsElectionOwner(accessToken: AccessToken, electionName:String){
        val election = findElection(electionName)
        requireIsElectionOwner(accessToken, election)
    }

    private fun requireIsElectionOwner(accessToken: AccessToken, election:ElectionRow){
        if(accessToken.userName != election.owner){
            fail(UNAUTHORIZED, "Election ${election.name} is owned by ${election.owner} not, ${accessToken.userName}")
        }
    }

    private fun requireHasNoEndDate(election:ElectionRow, message:String){
        if(election.noVotingAfter != null) {
            fail(UNSUPPORTED, message)
        }
    }

    private fun validateElectionUpdates(electionUpdates: ElectionUpdates): DbElectionUpdates {
        val dbElectionUpdates = electionUpdates.toDbElectionUpdates()
        val newName = dbElectionUpdates.newElectionName ?: return dbElectionUpdates
        val validNewName = validateString(newName, "electionUpdates.newName", Validation.electionName)
        return dbElectionUpdates.copy(newElectionName = validNewName)
    }

    private fun validateCandidateNames(candidateNames: List<String>):List<String> =
        validateStringList(candidateNames, "candidate", Validation.candidateName)

    private fun requireAfterElectionStarted(election:ElectionRow, now:Instant){
        val noVotingBefore = election.noVotingBefore ?: return
        if(now < noVotingBefore) {
            fail(UNSUPPORTED, "Election ${election.name} has not started yet")
        }
    }

    private fun requireAfterElectionEnded(election:ElectionRow, now:Instant){
        val noVotingBefore = election.noVotingAfter ?: return
        if(now < noVotingBefore) {
            fail(UNSUPPORTED, "Election ${election.name} has not ended yet")
        }
    }

    private fun requireElectionCanNotVote(election:ElectionRow){
        if(election.allowVote) {
            fail(UNSUPPORTED, "Election ${election.name} is still accepting votes")
        }
    }

    private fun requireElectionCanNotEdit(election:ElectionRow){
        if(election.allowEdit) {
            if(election.allowVote){
                fail(UNSUPPORTED, "Election ${election.name} has not launched yet")
            } else {
                fail(UNSUPPORTED, "Election ${election.name} has not been finalized yet")
            }
        }
    }

    private fun requireElectionCanEdit(election:ElectionRow){
        if(!election.allowEdit) {
            if(election.allowVote){
                fail(UNSUPPORTED, "Election ${election.name} is closed for edits once launched")
            } else {
                fail(UNSUPPORTED, "Election ${election.name} can not be edited once it has been finalized")
            }
        }
    }

    private fun requireNotLaunched(election:ElectionRow){
        if(election.allowEdit) {
            if(election.allowVote){
                fail(UNSUPPORTED, "Election ${election.name} is closed for edits once launched")
            }
        } else {
            if(election.allowVote){
                fail(UNSUPPORTED, "Election ${election.name} is closed for edits once launched")
            } else {
                fail(UNSUPPORTED, "Election ${election.name} can not be edited once it has been finalized")
            }
        }
    }

    private fun requireTallyAvailable(electionName:String, now: Instant){
        val election = findElection(electionName)
        requireAfterElectionStarted(election, now)
        if(!election.secretBallot) return
        requireAfterElectionEnded(election, now)
        requireElectionCanNotVote(election)
        requireElectionCanNotEdit(election)
    }

    private fun requireCanSeeBallot(accessToken: AccessToken, voterName:String, electionName:String, now:Instant){
        if(accessToken.userName == voterName) return
        val election = findElection(electionName)
        if(election.secretBallot){
            fail(UNAUTHORIZED, "Can only see your own ballot in secret ballot election $electionName")
        }
        requireTallyAvailable(electionName, now)
    }

    private fun candidateCount(electionName:String):Int =
        stateDbQueries.candidateCount(electionName)

    private fun requireHasMoreThanOneCandidate(electionName: String){
        val candidateCount = candidateCount(electionName)
        if(candidateCount < 2){
            fail(UNSUPPORTED, "Election can not be launched with less than 2 candidates, has $candidateCount")
        }
    }
}
