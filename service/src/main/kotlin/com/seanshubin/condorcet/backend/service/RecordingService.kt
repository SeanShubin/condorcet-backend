package com.seanshubin.condorcet.backend.service

import com.seanshubin.condorcet.backend.domain.*
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

class RecordingService(
    private val service: Service,
    private val serviceRequestEvent: (String) -> Unit,
    private val serviceResponseEvent: (String, String) -> Unit
) : Service {
    override fun synchronize() {
        val requestString = "synchronize()"
        serviceRequestEvent(requestString)
        val response = service.synchronize()
        serviceResponseEvent(requestString, response.toKotlinString())
        return response
    }

    override fun refresh(refreshToken: RefreshToken): Tokens {
        val requestString = "refresh(${refreshToken.toKotlinString()})"
        serviceRequestEvent(requestString)
        val response = service.refresh(refreshToken)
        serviceResponseEvent(requestString, response.toKotlinString())
        return response
    }

    override fun register(userName: String, email: String, password: String): Tokens {
        val requestString =
            "register(${userName.toKotlinString()}, ${email.toKotlinString()}, ${password.toKotlinString()})"
        serviceRequestEvent(requestString)
        val response = service.register(userName, email, password)
        serviceResponseEvent(requestString, response.toKotlinString())
        return response
    }

    override fun authenticate(nameOrEmail: String, password: String): Tokens {
        val requestString = "authenticate(${nameOrEmail.toKotlinString()}, ${password.toKotlinString()})"
        serviceRequestEvent(requestString)
        val response = service.authenticate(nameOrEmail, password)
        serviceResponseEvent(requestString, response.toKotlinString())
        return response
    }

    override fun permissionsForRole(role: Role): List<Permission> {
        val requestString = "permissionsForRole(${role.toKotlinString()})"
        serviceRequestEvent(requestString)
        val response = service.permissionsForRole(role)
        serviceResponseEvent(requestString, response.map { it.toKotlinString() }.toKotlinString())
        return response
    }

    override fun setRole(accessToken: AccessToken, userName: String, role: Role) {
        val requestString =
            "setRole(${accessToken.toKotlinString()}, ${userName.toKotlinString()}, ${role.toKotlinString()})"
        serviceRequestEvent(requestString)
        val response = service.setRole(accessToken, userName, role)
        serviceResponseEvent(requestString, response.toKotlinString())
        return response
    }

    override fun removeUser(accessToken: AccessToken, userName: String) {
        val requestString = "removeUser(${accessToken.toKotlinString()}, ${userName.toKotlinString()})"
        serviceRequestEvent(requestString)
        val response = service.removeUser(accessToken, userName)
        serviceResponseEvent(requestString, response.toKotlinString())
        return response
    }

    override fun listUsers(accessToken: AccessToken): List<UserNameRole> {
        val requestString = "listUsers(${accessToken.toKotlinString()})"
        serviceRequestEvent(requestString)
        val response = service.listUsers(accessToken)
        serviceResponseEvent(requestString, response.map { it.toKotlinString() }.toKotlinString())
        return response
    }

    override fun addElection(accessToken: AccessToken, electionName: String) {
        val requestString = "addElection(${accessToken.toKotlinString()}, ${electionName.toKotlinString()})"
        serviceRequestEvent(requestString)
        val response = service.addElection(accessToken, electionName)
        serviceResponseEvent(requestString, response.toKotlinString())
        return response
    }

    override fun launchElection(accessToken: AccessToken, electionName: String, allowEdit: Boolean) {
        val requestString =
            "launchElection(${accessToken.toKotlinString()}, ${electionName.toKotlinString()}, ${allowEdit.toKotlinString()})"
        serviceRequestEvent(requestString)
        val response = service.launchElection(accessToken, electionName, allowEdit)
        serviceResponseEvent(requestString, response.toKotlinString())
        return response
    }

    override fun finalizeElection(accessToken: AccessToken, electionName: String) {
        val requestString =
            "finalizeElection(${accessToken.toKotlinString()}, ${electionName.toKotlinString()})"
        serviceRequestEvent(requestString)
        val response = service.finalizeElection(accessToken, electionName)
        serviceResponseEvent(requestString, response.toKotlinString())
        return response
    }

    override fun updateElection(accessToken: AccessToken, electionName: String, electionUpdates: ElectionUpdates) {
        val requestString =
            "updateElection(${accessToken.toKotlinString()}, ${electionName.toKotlinString()}, ${electionUpdates.toKotlinString()})"
        serviceRequestEvent(requestString)
        val response = service.updateElection(accessToken, electionName, electionUpdates)
        serviceResponseEvent(requestString, response.toKotlinString())
        return response
    }

    override fun getElection(accessToken: AccessToken, electionName: String): ElectionDetail {
        val requestString = "getElection(${accessToken.toKotlinString()}, ${electionName.toKotlinString()})"
        serviceRequestEvent(requestString)
        val response = service.getElection(accessToken, electionName)
        serviceResponseEvent(requestString, response.toKotlinString())
        return response
    }

    override fun deleteElection(accessToken: AccessToken, electionName: String) {
        val requestString = "deleteElection(${accessToken.toKotlinString()}, ${electionName.toKotlinString()})"
        serviceRequestEvent(requestString)
        val response = service.deleteElection(accessToken, electionName)
        serviceResponseEvent(requestString, response.toKotlinString())
        return response
    }

    override fun listElections(accessToken: AccessToken): List<ElectionSummary> {
        val requestString = "listElections(${accessToken.toKotlinString()})"
        serviceRequestEvent(requestString)
        val response = service.listElections(accessToken)
        serviceResponseEvent(requestString, response.map { it.toKotlinString() }.toKotlinString())
        return response
    }

    override fun listTables(accessToken: AccessToken): List<String> {
        val requestString = "listTables(${accessToken.toKotlinString()})"
        serviceRequestEvent(requestString)
        val response = service.listTables(accessToken)
        serviceResponseEvent(requestString, response.map { it.toKotlinString() }.toKotlinString())
        return response
    }

    override fun userCount(accessToken: AccessToken): Int {
        val requestString = "userCount(${accessToken.toKotlinString()})"
        serviceRequestEvent(requestString)
        val response = service.userCount(accessToken)
        serviceResponseEvent(requestString, response.toKotlinString())
        return response
    }

    override fun electionCount(accessToken: AccessToken): Int {
        val requestString = "electionCount(${accessToken.toKotlinString()})"
        serviceRequestEvent(requestString)
        val response = service.electionCount(accessToken)
        serviceResponseEvent(requestString, response.toKotlinString())
        return response
    }

    override fun tableCount(accessToken: AccessToken): Int {
        val requestString = "tableCount(${accessToken.toKotlinString()})"
        serviceRequestEvent(requestString)
        val response = service.tableCount(accessToken)
        serviceResponseEvent(requestString, response.toKotlinString())
        return response
    }

    override fun eventCount(accessToken: AccessToken): Int {
        val requestString = "eventCount(${accessToken.toKotlinString()})"
        serviceRequestEvent(requestString)
        val response = service.eventCount(accessToken)
        serviceResponseEvent(requestString, response.toKotlinString())
        return response
    }

    override fun tableData(accessToken: AccessToken, tableName: String): TableData {
        val requestString = "tableData(${accessToken.toKotlinString()}, ${tableName.toKotlinString()})"
        serviceRequestEvent(requestString)
        val response = service.tableData(accessToken, tableName)
        serviceResponseEvent(requestString, response.toKotlinString())
        return response
    }

    override fun debugTableData(accessToken: AccessToken, tableName: String): TableData {
        val requestString = "debugTableData(${accessToken.toKotlinString()}, ${tableName.toKotlinString()})"
        serviceRequestEvent(requestString)
        val response = service.debugTableData(accessToken, tableName)
        serviceResponseEvent(requestString, response.toKotlinString())
        return response
    }

    override fun eventData(accessToken: AccessToken): TableData {
        val requestString = "eventData(${accessToken.toKotlinString()})"
        serviceRequestEvent(requestString)
        val response = service.eventData(accessToken)
        serviceResponseEvent(requestString, response.toKotlinString())
        return response
    }

    override fun setCandidates(accessToken: AccessToken, electionName: String, candidateNames: List<String>) {
        val requestString = "setCandidates(${accessToken.toKotlinString()}, ${electionName.toKotlinString()}, ${
            candidateNames.map { it.toKotlinString() }.toKotlinString()
        })"
        serviceRequestEvent(requestString)
        val response = service.setCandidates(accessToken, electionName, candidateNames)
        serviceResponseEvent(requestString, response.toKotlinString())
        return response
    }

    override fun listCandidates(accessToken: AccessToken, electionName: String): List<String> {
        val requestString = "listCandidates(${accessToken.toKotlinString()}, ${electionName.toKotlinString()})"
        serviceRequestEvent(requestString)
        val response = service.listCandidates(accessToken, electionName)
        serviceResponseEvent(requestString, response.map { it.toKotlinString() }.toKotlinString())
        return response
    }

    override fun castBallot(
        accessToken: AccessToken,
        voterName: String,
        electionName: String,
        rankings: List<Ranking>
    ) {
        val requestString =
            "castBallot(${accessToken.toKotlinString()}, ${voterName.toKotlinString()}, ${electionName.toKotlinString()}, ${
                rankings.map { it.toKotlinString() }.toKotlinString()
            })"
        serviceRequestEvent(requestString)
        val response = service.castBallot(accessToken, voterName, electionName, rankings)
        serviceResponseEvent(requestString, response.toKotlinString())
        return response
    }

    override fun listRankings(accessToken: AccessToken, voterName: String, electionName: String): List<Ranking> {
        val requestString =
            "listRankings(${accessToken.toKotlinString()}, ${voterName.toKotlinString()}, ${electionName.toKotlinString()})"
        serviceRequestEvent(requestString)
        val response = service.listRankings(accessToken, voterName, electionName)
        serviceResponseEvent(requestString, response.map { it.toKotlinString() }.toKotlinString())
        return response
    }

    override fun tally(accessToken: AccessToken, electionName: String): Tally {
        val requestString = "tally(${accessToken.toKotlinString()}, ${electionName.toKotlinString()})"
        serviceRequestEvent(requestString)
        val response = service.tally(accessToken, electionName)
        serviceResponseEvent(requestString, response.toKotlinString())
        return response
    }

    override fun listEligibility(accessToken: AccessToken, electionName: String): List<VoterEligibility> {
        val requestString = "listEligibility(${accessToken.toKotlinString()}, ${electionName.toKotlinString()})"
        serviceRequestEvent(requestString)
        val response = service.listEligibility(accessToken, electionName)
        serviceResponseEvent(requestString, response.map { it.toKotlinString() }.toKotlinString())
        return response
    }

    override fun setEligibleVoters(accessToken: AccessToken, electionName: String, userNames: List<String>) {
        val requestString = "setEligibleVoters(${accessToken.toKotlinString()}, ${electionName.toKotlinString()}, ${
            userNames.map { it.toKotlinString() }.toKotlinString()
        })"
        serviceRequestEvent(requestString)
        val response = service.setEligibleVoters(accessToken, electionName, userNames)
        serviceResponseEvent(requestString, response.toKotlinString())
        return response
    }

    override fun isEligible(accessToken: AccessToken, userName: String, electionName: String): Boolean {
        val requestString =
            "isEligible(${accessToken.toKotlinString()}, ${userName.toKotlinString()}, ${electionName.toKotlinString()})"
        serviceRequestEvent(requestString)
        val response = service.isEligible(accessToken, userName, electionName)
        serviceResponseEvent(requestString, response.toKotlinString())
        return response
    }

    override fun getBallot(accessToken: AccessToken, voterName: String, electionName: String): BallotSummary? {
        val requestString =
            "getBallot(${accessToken.toKotlinString()}, ${voterName.toKotlinString()}, ${electionName.toKotlinString()})"
        serviceRequestEvent(requestString)
        val response = service.getBallot(accessToken, voterName, electionName)
        serviceResponseEvent(requestString, response.toKotlinString())
        return response
    }

    private fun String?.toKotlinString(): String = if (this == null) "null" else "\"${this.escape()}\""

    private fun Boolean?.toKotlinString(): String = if (this == null) "null" else "$this"

    private fun Int?.toKotlinString(): String = if (this == null) "null" else "$this"

    private fun Instant?.toKotlinString(): String = if (this == null) "null" else "Instant.parse(\"$this\")"

    private fun Unit.toKotlinString(): String = "Unit"

    private fun Role.toKotlinString(): String = toString()

    private fun RefreshToken.toKotlinString(): String =
        "RefreshToken(${userName.toKotlinString()})"

    private fun AccessToken.toKotlinString(): String =
        "AccessToken(${userName.toKotlinString()}, ${role.toKotlinString()})"

    private fun Tokens.toKotlinString(): String =
        "Tokens(${refreshToken.toKotlinString()},${accessToken.toKotlinString()})"

    private fun Permission.toKotlinString(): String = this.toString()

    private fun UserNameRole.toKotlinString(): String =
        "UserNameRole(${userName.toKotlinString()}, ${role.toKotlinString()}, ${
            allowedRoles.map { it.toKotlinString() }.toKotlinString()
        })"

    private fun ElectionUpdates.toKotlinString(): String =
        "ElectionUpdates(" +
                "${newElectionName.toKotlinString()}, " +
                "${secretBallot.toKotlinString()}, " +
                "${clearNoVotingBefore.toKotlinString()}, " +
                "${noVotingBefore.toKotlinString()}, " +
                "${clearNoVotingAfter.toKotlinString()}, " +
                "${noVotingAfter})"

    private fun VoterEligibility.toKotlinString(): String =
        "VoterEligibility(${voterName.toKotlinString()}, ${eligible.toKotlinString()})"

    private fun Ranking.toKotlinString(): String =
        "Ranking(${candidateName.toKotlinString()}, ${rank.toKotlinString()})"

    private fun ElectionDetail.toKotlinString(): String =
        "ElectionDetail(" +
                "${ownerName.toKotlinString()}, " +
                "${electionName.toKotlinString()}, " +
                "${candidateCount.toKotlinString()}, " +
                "${voterCount.toKotlinString()}, " +
                "${secretBallot.toKotlinString()}, " +
                "${noVotingBefore.toKotlinString()}, " +
                "${noVotingAfter.toKotlinString()}, " +
                "${allowEdit.toKotlinString()}, " +
                "${allowVote.toKotlinString()})"

    private fun ElectionSummary.toKotlinString(): String =
        "ElectionSummary(" +
                "${ownerName.toKotlinString()}, " +
                "${electionName.toKotlinString()}, " +
                "${secretBallot.toKotlinString()}, " +
                "${noVotingBefore.toKotlinString()}, " +
                "${noVotingAfter.toKotlinString()}, " +
                "${allowEdit.toKotlinString()}, " +
                "${allowVote.toKotlinString()})"

    private fun Ballot.toKotlinString(): String =
        "Ballot(" +
                "${voterName.toKotlinString()}, " +
                "${electionName.toKotlinString()}, " +
                "${confirmation.toKotlinString()}, " +
                "${whenCast.toKotlinString()}, " +
                rankings.map { it.toKotlinString() }.toKotlinString() +
                ")"

    private fun Preference.toKotlinString(): String =
        path.windowed(2).zip(strengths).joinToString(" + ") { (ab, c) ->
            val (a, b) = ab
            """Preference("$a", $c, "$b")"""
        }

    private fun listPreferenceToKotlinString(list: List<Preference>): String =
        list.map { it.toKotlinString() }.toKotlinString()

    private fun listListPreferenceToKotlinString(list: List<List<Preference>>): String =
        list.map { listPreferenceToKotlinString(it) }.toKotlinString()

    private fun Tally.toKotlinString(): String =
        "Tally(" +
                "${candidateNames.map { it.toKotlinString() }.toKotlinString()}, " +
                "${secretBallot.toKotlinString()}, " +
                "${ballots.map { it.toKotlinString() }.toKotlinString()}, " +
                "${listListPreferenceToKotlinString(preferences)}, " +
                "${listListPreferenceToKotlinString(strongestPathMatrix)}, " +
                "${places.map { it.toKotlinString() }.toKotlinString()}, " +
                "${whoVoted.map { it.toKotlinString() }.toKotlinString()})"

    private fun BallotSummary?.toKotlinString(): String = if (this == null) "null" else
        "BallotSummary(" +
                "${voterName.toKotlinString()}, " +
                "${electionName.toKotlinString()}, " +
                "${confirmation.toKotlinString()}, " +
                "${whenCast.toKotlinString()})"

    private fun anyToKotlinString(any: Any?): String = when (any) {
        null -> "null"
        is String -> any.toKotlinString()
        is Int -> any.toKotlinString()
        is Boolean -> any.toKotlinString()
        is LocalDateTime -> any.toInstant(ZoneOffset.UTC).toKotlinString()
        else -> throw UnsupportedOperationException("${any.javaClass.name} not implemented")
    }

    private fun listAnyToKotlinString(list: List<Any?>): String =
        list.map { anyToKotlinString(it) }.toKotlinString()

    private fun listListAnyToKotlinString(list: List<List<Any?>>): String =
        list.map { listAnyToKotlinString(it) }.toKotlinString()

    private fun TableData.toKotlinString(): String {
        val columnNamesKotlinString = columnNames.map { it.toKotlinString() }.toKotlinString()
        val rowsKotlinString = listListAnyToKotlinString(rows)
        return "TableData($columnNamesKotlinString, $rowsKotlinString)"
    }

    private fun List<String>.toKotlinString(): String =
        joinToString(",", "listOf(", ")") { it }

    private fun String.escape(): String = this.flatMap(::escapeCharToIterable).joinToString("")
    private fun escapeCharToIterable(target: Char): Iterable<Char> = escapeCharToString(target).asIterable()
    private fun escapeCharToString(target: Char): String =
        when (target) {
            '\n' -> "\\n"
            '\b' -> "\\b"
            '\t' -> "\\t"
            '\r' -> "\\r"
            '\"' -> "\\\""
            '\'' -> "\\\'"
            '\\' -> "\\\\"
            else -> target.toString()
        }
}
