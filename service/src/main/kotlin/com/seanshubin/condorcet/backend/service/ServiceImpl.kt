package com.seanshubin.condorcet.backend.service

import arrow.core.Either
import com.seanshubin.condorcet.backend.crypto.PasswordUtil
import com.seanshubin.condorcet.backend.database.*
import com.seanshubin.condorcet.backend.database.DbElectionUpdates.Companion.toDbElectionUpdates
import com.seanshubin.condorcet.backend.domain.*
import com.seanshubin.condorcet.backend.domain.Permission.*
import com.seanshubin.condorcet.backend.domain.Ranking.Companion.addMissingCandidates
import com.seanshubin.condorcet.backend.domain.Ranking.Companion.voterBiasedOrdering
import com.seanshubin.condorcet.backend.domain.Role.OWNER
import com.seanshubin.condorcet.backend.domain.Role.UNASSIGNED
import com.seanshubin.condorcet.backend.genericdb.GenericTable
import com.seanshubin.condorcet.backend.service.CaseInsensitiveStringListUtil.extra
import com.seanshubin.condorcet.backend.service.CaseInsensitiveStringListUtil.missing
import com.seanshubin.condorcet.backend.service.DataTransfer.toDomain
import com.seanshubin.condorcet.backend.service.ServiceException.Category.*
import kotlin.random.Random

class ServiceImpl(
    private val passwordUtil: PasswordUtil,
    private val eventDbQueries: EventDbQueries,
    private val stateDbQueries: StateDbQueries,
    private val stateDbCommands: StateDbCommands,
    private val synchronizer: Synchronizer,
    private val random: Random
) : Service {
    override fun synchronize() {
        synchronizer.synchronize()
    }

    override fun refresh(refreshToken: RefreshToken): Tokens {
        val userRow = searchUserByName(refreshToken.userName)
        failIf(userRow == null, NOT_FOUND, "User with name '${refreshToken.userName}' not found")
        userRow!!
        val accessToken = AccessToken(userRow.name, userRow.role)
        return Tokens(refreshToken, accessToken)
    }

    override fun register(name: String, email: String, password: String): Tokens {
        val validName = validateString(name, "name", Validation.userName)
        val validEmail = validateString(email, "email", Validation.email)
        val validPassword = validateString(password, "password", Validation.password)
        failIf(userNameExists(validName), CONFLICT, "User with name '$validName' already exists")
        failIf(userEmailExists(validEmail), CONFLICT, "User with email '$validEmail' already exists")
        val role = if (stateDbQueries.userCount() == 0) {
            OWNER
        } else {
            UNASSIGNED
        }
        val (salt, hash) = passwordUtil.createSaltAndHash(validPassword)
        val authority = validName
        stateDbCommands.createUser(authority, validName, validEmail, salt, hash, role)
        val user = stateDbQueries.findUserByName(validName)
        return createTokens(user)
    }

    override fun authenticate(nameOrEmail: String, password: String): Tokens {
        val userRow = searchUserByNameOrEmail(nameOrEmail)
        failIf(userRow == null, NOT_FOUND, "User with name or email '$nameOrEmail' does not exist")
        userRow!!
        val passwordMatches = passwordUtil.passwordMatches(password, userRow.salt, userRow.hash)
        failUnless(passwordMatches, UNAUTHORIZED, "Authentication failed for user with name or email '$nameOrEmail'")
        return createTokens(userRow)
    }

    override fun setRole(accessToken: AccessToken, name: String, role: Role) {
        val userRow = searchUserByName(name)
        failIf(userRow == null, NOT_FOUND, "User with name '$name' does not exist")
        userRow!!
        failUnlessPermission(accessToken, MANAGE_USERS)
        val isChangingSelfRole = isSelf(accessToken, userRow) && accessToken.role != role
        failIf(isChangingSelfRole, UNAUTHORIZED, "Not allowed to change role for self")
        failUnless(roleIsGreater(accessToken, userRow), UNAUTHORIZED, "Must have greater role than target")
        failUnless(roleIsGreater(accessToken, role), UNAUTHORIZED, "Not allowed to set roles greater or equal to self")
        stateDbCommands.setRole(accessToken.userName, name, role)
    }

    override fun removeUser(accessToken: AccessToken, name: String) {
        val userRow = searchUserByName(name)
        failIf(userRow == null, NOT_FOUND, "User with name '$name' does not exist")
        userRow!!
        failUnlessPermission(accessToken, MANAGE_USERS)
        failIf(
            isSelf(accessToken, userRow) && stateDbQueries.tableCount() > 1, UNAUTHORIZED,
            "Not allowed to remove self unless you are the only user"
        )
        failUnless(roleIsGreater(accessToken, userRow), UNAUTHORIZED, "Must have greater role than target")
        stateDbCommands.removeUser(accessToken.userName, name)
    }

    override fun listUsers(accessToken: AccessToken): List<UserNameRole> {
        failUnlessPermission(accessToken, MANAGE_USERS)
        val userRows = stateDbQueries.listUsers()
        val list = userRows.map { row ->
            val allowedRoles = Role.values().filter {
                allowedToChangeRoleTo(accessToken, row, it)
            }
            UserNameRole(row.name, row.role, allowedRoles)
        }
        return list
    }

    override fun addElection(accessToken: AccessToken, name: String) {
        val validName = validateString(name, "name", Validation.electionName)
        failUnlessPermission(accessToken, USE_APPLICATION)
        failIf(electionNameExists(validName), CONFLICT, "Election with name '$validName' already exists")
        stateDbCommands.addElection(accessToken.userName, accessToken.userName, validName)
    }

    override fun launchElection(accessToken: AccessToken, name: String, allowEdit: Boolean) {
        failUnlessPermission(accessToken, USE_APPLICATION)
        failUnlessElectionOwner(accessToken, name)
        val updates = DbElectionUpdates(allowVote = true, allowEdit = allowEdit)
        stateDbCommands.updateElection(accessToken.userName, name, updates)
    }

    override fun finalizeElection(accessToken: AccessToken, name: String) {
        failUnlessPermission(accessToken, USE_APPLICATION)
        failUnlessElectionOwner(accessToken, name)
        val updates = DbElectionUpdates(allowVote = false, allowEdit = false)
        stateDbCommands.updateElection(accessToken.userName, name, updates)
    }

    private fun validateElectionUpdates(electionUpdates: ElectionUpdates): DbElectionUpdates {
        val dbElectionUpdates = electionUpdates.toDbElectionUpdates()
        val newName = dbElectionUpdates.newName ?: return dbElectionUpdates
        val validNewName = validateString(newName, "electionUpdates.newName", Validation.electionName)
        return dbElectionUpdates.copy(newName = validNewName)
    }

    override fun updateElection(accessToken: AccessToken, name: String, electionUpdates: ElectionUpdates) {
        failUnlessPermission(accessToken, USE_APPLICATION)
        failUnlessElectionOwner(accessToken, name)
        val validElectionUpdates = validateElectionUpdates(electionUpdates)
        stateDbCommands.updateElection(accessToken.userName, name, validElectionUpdates)
    }

    override fun getElection(accessToken: AccessToken, name: String): ElectionDetail {
        failUnlessPermission(accessToken, USE_APPLICATION)
        val electionRow = stateDbQueries.searchElectionByName(name)
        val candidateCount = stateDbQueries.candidateCount(name)
        val voterCount = stateDbQueries.voterCount(name)
        failIf(electionRow == null, NOT_FOUND, "Election with name '$name' not found")
        electionRow!!
        val election = electionRow.toDomain(candidateCount, voterCount)
        return election
    }

    override fun deleteElection(accessToken: AccessToken, name: String) {
        failUnlessPermission(accessToken, USE_APPLICATION)
        val electionRow = stateDbQueries.searchElectionByName(name)
        failIf(electionRow == null, NOT_FOUND, "Election with name '$name' not found")
        electionRow!!
        failIf(
            accessToken.userName != electionRow.owner,
            UNAUTHORIZED,
            "User '${accessToken.userName}' is not allowed to delete election '$name' owned by user '${electionRow.owner}'"
        )
        stateDbCommands.deleteElection(accessToken.userName, name)
    }

    override fun listElections(accessToken: AccessToken): List<ElectionSummary> {
        failUnlessPermission(accessToken, USE_APPLICATION)
        val rows = stateDbQueries.listElections()
        val list = rows.map { it.toDomain() }
        return list
    }

    override fun listTables(accessToken: AccessToken): List<String> {
        failUnlessPermission(accessToken, VIEW_SECRETS)
        return stateDbQueries.tableNames(StateSchema)
    }

    override fun userCount(accessToken: AccessToken): Int {
        failUnlessPermission(accessToken, VIEW_APPLICATION)
        return stateDbQueries.userCount()
    }

    override fun electionCount(accessToken: AccessToken): Int {
        failUnlessPermission(accessToken, VIEW_APPLICATION)
        return stateDbQueries.electionCount()
    }

    override fun tableCount(accessToken: AccessToken): Int {
        failUnlessPermission(accessToken, VIEW_APPLICATION)
        return stateDbQueries.tableCount()
    }

    override fun eventCount(accessToken: AccessToken): Int {
        failUnlessPermission(accessToken, VIEW_APPLICATION)
        return eventDbQueries.eventCount()
    }

    override fun tableData(accessToken: AccessToken, name: String): TableData {
        failUnlessPermission(accessToken, VIEW_SECRETS)
        val genericTable = stateDbQueries.tableData(StateSchema, name)
        return genericTable.toTableData()
    }

    override fun debugTableData(accessToken: AccessToken, name: String): TableData {
        failUnlessPermission(accessToken, VIEW_SECRETS)
        val genericTable = stateDbQueries.debugTableData(StateSchema, name)
        return genericTable.toTableData()
    }

    override fun eventData(accessToken: AccessToken): TableData {
        failUnlessPermission(accessToken, VIEW_SECRETS)
        val genericTable = eventDbQueries.tableData(EventSchema, "event")
        return genericTable.toTableData()
    }

    override fun setCandidates(accessToken: AccessToken, electionName: String, candidateNames: List<String>) {
        failUnlessPermission(accessToken, USE_APPLICATION)
        failUnlessElectionOwner(accessToken, electionName)
        val validCandidateNames = validateStringList(candidateNames, "candidate", Validation.candidateName)
        val originalCandidates = stateDbQueries.listCandidates(electionName)
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
        failUnlessPermission(accessToken, USE_APPLICATION)
        val candidateNames = stateDbQueries.listCandidates(electionName)
        return candidateNames
    }

    override fun castBallot(
        accessToken: AccessToken,
        voterName: String,
        electionName: String,
        rankings: List<Ranking>
    ) {
        failUnlessPermission(accessToken, USE_APPLICATION)
        val electionRow = stateDbQueries.searchElectionByName(electionName)
        failIf(electionRow == null, NOT_FOUND, "Election with name '$electionName' not found")
        electionRow!!
        val voterRow = stateDbQueries.searchUserByName(voterName)
        failIf(voterRow == null, NOT_FOUND, "Voter with name '$voterName' not found")
        voterRow!!
        if (accessToken.userName != voterName) {
            throw ServiceException(
                UNAUTHORIZED,
                "User '${accessToken.userName}' not allowed to cast a ballot on behalf of voter '$voterName'"
            )
        }
        val ballotRow = stateDbQueries.searchBallot(voterName, electionName)
        if (ballotRow != null) {
            stateDbCommands.rescindBallot(accessToken.userName, voterName, electionName)
        }
        stateDbCommands.castBallot(accessToken.userName, voterName, electionName, rankings)
    }

    override fun listRankings(accessToken: AccessToken, voterName: String, electionName: String): List<Ranking> {
        failUnlessPermission(accessToken, USE_APPLICATION)
        if (accessToken.userName != voterName) {
            throw ServiceException(
                UNAUTHORIZED,
                "User '${accessToken.userName}' not allowed to see a ballot cast by voter '$voterName'"
            )
        }
        val candidates = stateDbQueries.listCandidates(electionName)
        val rankings = stateDbQueries.listRankings(voterName, electionName)
        return rankings.addMissingCandidates(candidates).voterBiasedOrdering(random)
    }

    override fun tally(accessToken: AccessToken, electionName: String): Tally {
        failUnlessPermission(accessToken, USE_APPLICATION)
        val electionRow = findElection(electionName)
        val secretBallot = electionRow.secretBallot
        val candidates = stateDbQueries.listCandidates(electionName)
        val ballots = stateDbQueries.listBallots(electionName)
        val tally = Tally.countBallots(secretBallot, candidates, ballots)
        return tally
    }

    override fun listEligibility(accessToken: AccessToken, electionName: String): List<VoterEligibility> {
        failUnlessPermission(accessToken, USE_APPLICATION)
        val allVoters = stateDbQueries.listVoterNames()
        val eligibleVoters = stateDbQueries.listVotersForElection(electionName)
        val list = allVoters.map {
            val eligible = eligibleVoters.contains(it)
            VoterEligibility(it, eligible)
        }
        return list
    }

    override fun setEligibleVoters(accessToken: AccessToken, electionName: String, voterNames: List<String>) {
        failUnlessPermission(accessToken, USE_APPLICATION)
        failUnlessElectionOwner(accessToken, electionName)
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

    override fun isEligible(accessToken: AccessToken, userName: String, electionName: String): Boolean {
        failUnlessPermission(accessToken, USE_APPLICATION)
        val eligibleVoters = stateDbQueries.listVotersForElection(electionName)
        return eligibleVoters.contains(userName)
    }

    private fun userNameExists(name: String): Boolean = stateDbQueries.searchUserByName(name) != null
    private fun userEmailExists(email: String): Boolean = stateDbQueries.searchUserByEmail(email) != null
    private fun electionNameExists(name: String): Boolean = stateDbQueries.searchElectionByName(name) != null
    private fun roleIsGreater(accessToken: AccessToken, userRow: UserRow): Boolean =
        roleIsGreater(accessToken.role, userRow.role)

    private fun roleIsGreater(accessToken: AccessToken, role: Role): Boolean = roleIsGreater(accessToken.role, role)
    private fun roleIsGreater(first: Role, second: Role): Boolean = first.ordinal < second.ordinal
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

    private fun failUnlessPermission(accessToken: AccessToken, permission: Permission) {
        failUnless(
            hasPermission(accessToken, permission), UNAUTHORIZED,
            "User ${accessToken.userName} with role ${accessToken.role} does not have permission $permission"
        )

    }

    private fun allowedToChangeRoleTo(accessToken: AccessToken, userRow: UserRow, role: Role): Boolean {
        if (userRow.role == role) return true
        if (!hasPermission(accessToken, MANAGE_USERS)) return false
        if (isSelf(accessToken, userRow)) return false
        if (!roleIsGreater(accessToken, userRow)) return false
        if (!roleIsGreater(accessToken, role)) return false
        return true
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
            is Either.Right -> validated.value
            is Either.Left -> fail(UNSUPPORTED, "Invalid $caption, ${validated.value}")
        }

    private fun validateStringList(
        original: List<String>,
        caption: String,
        rule: (String) -> Either<String, String>
    ): List<String> =
        original.filter { it.trim() != "" }.map { validateString(it, "$caption '$it'", rule) }

    private fun findElection(electionName:String):ElectionRow {
        val electionRow = stateDbQueries.searchElectionByName(electionName)
        failIf(electionRow == null, NOT_FOUND, "Election with name '$electionName' not found")
        return electionRow!!
    }

    private fun failUnlessElectionOwner(accessToken: AccessToken, electionName: String) {
        val electionRow = findElection(electionName)
        failIf(
            accessToken.userName != electionRow.owner,
            UNAUTHORIZED,
            "User '${accessToken.userName}' does not own election '$electionName', it is owned by user '${electionRow.owner}'"
        )
    }

    private fun validateVoterNames(voterNames: List<String>) {
        val userNames = stateDbQueries.listUserNames()
        val invalidNames = voterNames.filterNot { userNames.contains(it) }
        if (invalidNames.isNotEmpty()) {
            val invalidNamesString = invalidNames.joinToString("', '", "'", "'")
            fail(UNSUPPORTED, "The following are not valid user names: $invalidNamesString")
        }
    }
}
