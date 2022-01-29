package com.seanshubin.condorcet.backend.service

import com.seanshubin.condorcet.backend.domain.*
import com.seanshubin.condorcet.backend.service.ToKotlinString.toKotlinString

class RecordingService(
    private val service: Service,
    private val serviceRequestEvent: (String, String) -> Unit,
    private val serviceResponseEvent: (String, String, String) -> Unit
) : Service {
    override fun synchronize() {
        val requestString = ""
        serviceRequestEvent("synchronize", requestString)
        val response = service.synchronize()
        serviceResponseEvent("synchronize", requestString, response.toKotlinString())
        return response
    }

    override fun refresh(refreshToken: RefreshToken): Tokens {
        val requestString = refreshToken.toKotlinString()
        serviceRequestEvent("refresh", requestString)
        val response = service.refresh(refreshToken)
        serviceResponseEvent("refresh", requestString, response.toKotlinString())
        return response
    }

    override fun register(userName: String, email: String, password: String): Tokens {
        val requestString =
            "${userName.toKotlinString()}, ${email.toKotlinString()}, ${password.toKotlinString()}"
        serviceRequestEvent("register",requestString)
        val response = service.register(userName, email, password)
        serviceResponseEvent("register",requestString, response.toKotlinString())
        return response
    }

    override fun authenticate(nameOrEmail: String, password: String): Tokens {
        val requestString = "${nameOrEmail.toKotlinString()}, ${password.toKotlinString()}"
        serviceRequestEvent("authenticate", requestString)
        val response = service.authenticate(nameOrEmail, password)
        serviceResponseEvent("authenticate", requestString, response.toKotlinString())
        return response
    }

    override fun permissionsForRole(role: Role): List<Permission> {
        val requestString = role.toKotlinString()
        serviceRequestEvent("permissionsForRole", requestString)
        val response = service.permissionsForRole(role)
        serviceResponseEvent("permissionsForRole", requestString, response.map { it.toKotlinString() }.toKotlinString())
        return response
    }

    override fun setRole(accessToken: AccessToken, userName: String, role: Role) {
        val requestString =
            "${accessToken.toKotlinString()}, ${userName.toKotlinString()}, ${role.toKotlinString()}"
        serviceRequestEvent("setRole", requestString)
        val response = service.setRole(accessToken, userName, role)
        serviceResponseEvent("setRole", requestString, response.toKotlinString())
        return response
    }

    override fun removeUser(accessToken: AccessToken, userName: String) {
        val requestString = "${accessToken.toKotlinString()}, ${userName.toKotlinString()}"
        serviceRequestEvent("removeUser", requestString)
        val response = service.removeUser(accessToken, userName)
        serviceResponseEvent("removeUser", requestString, response.toKotlinString())
        return response
    }

    override fun listUsers(accessToken: AccessToken): List<UserNameRole> {
        val requestString = accessToken.toKotlinString()
        serviceRequestEvent("listUsers", requestString)
        val response = service.listUsers(accessToken)
        serviceResponseEvent("listUsers", requestString, response.map { it.toKotlinString() }.toKotlinString())
        return response
    }

    override fun addElection(accessToken: AccessToken, electionName: String) {
        val requestString = "${accessToken.toKotlinString()}, ${electionName.toKotlinString()}"
        serviceRequestEvent("addElection", requestString)
        val response = service.addElection(accessToken, electionName)
        serviceResponseEvent("addElection", requestString, response.toKotlinString())
        return response
    }

    override fun launchElection(accessToken: AccessToken, electionName: String, allowEdit: Boolean) {
        val requestString =
            "${accessToken.toKotlinString()}, ${electionName.toKotlinString()}, ${allowEdit.toKotlinString()}"
        serviceRequestEvent("launchElection", requestString)
        val response = service.launchElection(accessToken, electionName, allowEdit)
        serviceResponseEvent("launchElection", requestString, response.toKotlinString())
        return response
    }

    override fun finalizeElection(accessToken: AccessToken, electionName: String) {
        val requestString =
            "${accessToken.toKotlinString()}, ${electionName.toKotlinString()}"
        serviceRequestEvent("finalizeElection", requestString)
        val response = service.finalizeElection(accessToken, electionName)
        serviceResponseEvent("finalizeElection", requestString, response.toKotlinString())
        return response
    }

    override fun updateElection(accessToken: AccessToken, electionName: String, electionUpdates: ElectionUpdates) {
        val requestString =
            "${accessToken.toKotlinString()}, ${electionName.toKotlinString()}, ${electionUpdates.toKotlinString()}"
        serviceRequestEvent("updateElection", requestString)
        val response = service.updateElection(accessToken, electionName, electionUpdates)
        serviceResponseEvent("updateElection", requestString, response.toKotlinString())
        return response
    }

    override fun getElection(accessToken: AccessToken, electionName: String): ElectionDetail {
        val requestString = "${accessToken.toKotlinString()}, ${electionName.toKotlinString()}"
        serviceRequestEvent("getElection", requestString)
        val response = service.getElection(accessToken, electionName)
        serviceResponseEvent("getElection", requestString, response.toKotlinString())
        return response
    }

    override fun deleteElection(accessToken: AccessToken, electionName: String) {
        val requestString = "${accessToken.toKotlinString()}, ${electionName.toKotlinString()}"
        serviceRequestEvent("deleteElection", requestString)
        val response = service.deleteElection(accessToken, electionName)
        serviceResponseEvent("deleteElection", requestString, response.toKotlinString())
        return response
    }

    override fun listElections(accessToken: AccessToken): List<ElectionSummary> {
        val requestString = accessToken.toKotlinString()
        serviceRequestEvent("listElections", requestString)
        val response = service.listElections(accessToken)
        serviceResponseEvent("listElections", requestString, response.map { it.toKotlinString() }.toKotlinString())
        return response
    }

    override fun listTables(accessToken: AccessToken): List<String> {
        val requestString = accessToken.toKotlinString()
        serviceRequestEvent("listTables", requestString)
        val response = service.listTables(accessToken)
        serviceResponseEvent("listTables", requestString, response.map { it.toKotlinString() }.toKotlinString())
        return response
    }

    override fun userCount(accessToken: AccessToken): Int {
        val requestString = accessToken.toKotlinString()
        serviceRequestEvent("userCount", requestString)
        val response = service.userCount(accessToken)
        serviceResponseEvent("userCount", requestString, response.toKotlinString())
        return response
    }

    override fun electionCount(accessToken: AccessToken): Int {
        val requestString = accessToken.toKotlinString()
        serviceRequestEvent("electionCount", requestString)
        val response = service.electionCount(accessToken)
        serviceResponseEvent("electionCount", requestString, response.toKotlinString())
        return response
    }

    override fun tableCount(accessToken: AccessToken): Int {
        val requestString = accessToken.toKotlinString()
        serviceRequestEvent("tableCount", requestString)
        val response = service.tableCount(accessToken)
        serviceResponseEvent("tableCount", requestString, response.toKotlinString())
        return response
    }

    override fun eventCount(accessToken: AccessToken): Int {
        val requestString = accessToken.toKotlinString()
        serviceRequestEvent("eventCount", requestString)
        val response = service.eventCount(accessToken)
        serviceResponseEvent("eventCount", requestString, response.toKotlinString())
        return response
    }

    override fun tableData(accessToken: AccessToken, tableName: String): TableData {
        val requestString = "${accessToken.toKotlinString()}, ${tableName.toKotlinString()}"
        serviceRequestEvent("tableData", requestString)
        val response = service.tableData(accessToken, tableName)
        serviceResponseEvent("tableData", requestString, response.toKotlinString())
        return response
    }

    override fun debugTableData(accessToken: AccessToken, tableName: String): TableData {
        val requestString = "${accessToken.toKotlinString()}, ${tableName.toKotlinString()}"
        serviceRequestEvent("debugTableData", requestString)
        val response = service.debugTableData(accessToken, tableName)
        serviceResponseEvent("debugTableData", requestString, response.toKotlinString())
        return response
    }

    override fun eventData(accessToken: AccessToken): TableData {
        val requestString = accessToken.toKotlinString()
        serviceRequestEvent("eventData", requestString)
        val response = service.eventData(accessToken)
        serviceResponseEvent("eventData", requestString, response.toKotlinString())
        return response
    }

    override fun setCandidates(accessToken: AccessToken, electionName: String, candidateNames: List<String>) {
        val requestString = "${accessToken.toKotlinString()}, ${electionName.toKotlinString()}, ${
            candidateNames.map { it.toKotlinString() }.toKotlinString()
        }"
        serviceRequestEvent("setCandidates", requestString)
        val response = service.setCandidates(accessToken, electionName, candidateNames)
        serviceResponseEvent("setCandidates", requestString, response.toKotlinString())
        return response
    }

    override fun listCandidates(accessToken: AccessToken, electionName: String): List<String> {
        val requestString = "${accessToken.toKotlinString()}, ${electionName.toKotlinString()}"
        serviceRequestEvent("listCandidates", requestString)
        val response = service.listCandidates(accessToken, electionName)
        serviceResponseEvent("listCandidates", requestString, response.map { it.toKotlinString() }.toKotlinString())
        return response
    }

    override fun castBallot(
        accessToken: AccessToken,
        voterName: String,
        electionName: String,
        rankings: List<Ranking>
    ) {
        val requestString =
            "${accessToken.toKotlinString()}, ${voterName.toKotlinString()}, ${electionName.toKotlinString()}, ${
                rankings.map { it.toKotlinString() }.toKotlinString()
            }"
        serviceRequestEvent("castBallot", requestString)
        val response = service.castBallot(accessToken, voterName, electionName, rankings)
        serviceResponseEvent("castBallot", requestString, response.toKotlinString())
        return response
    }

    override fun listRankings(accessToken: AccessToken, voterName: String, electionName: String): List<Ranking> {
        val requestString =
            "${accessToken.toKotlinString()}, ${voterName.toKotlinString()}, ${electionName.toKotlinString()}"
        serviceRequestEvent("listRankings", requestString)
        val response = service.listRankings(accessToken, voterName, electionName)
        serviceResponseEvent("listRankings", requestString, response.map { it.toKotlinString() }.toKotlinString())
        return response
    }

    override fun tally(accessToken: AccessToken, electionName: String): Tally {
        val requestString = "${accessToken.toKotlinString()}, ${electionName.toKotlinString()}"
        serviceRequestEvent("tally", requestString)
        val response = service.tally(accessToken, electionName)
        serviceResponseEvent("tally", requestString, response.toKotlinString())
        return response
    }

    override fun listEligibility(accessToken: AccessToken, electionName: String): List<VoterEligibility> {
        val requestString = "${accessToken.toKotlinString()}, ${electionName.toKotlinString()}"
        serviceRequestEvent("listEligibility", requestString)
        val response = service.listEligibility(accessToken, electionName)
        serviceResponseEvent("listEligibility", requestString, response.map { it.toKotlinString() }.toKotlinString())
        return response
    }

    override fun setEligibleVoters(accessToken: AccessToken, electionName: String, userNames: List<String>) {
        val requestString = "${accessToken.toKotlinString()}, ${electionName.toKotlinString()}, ${
            userNames.map { it.toKotlinString() }.toKotlinString()
        }"
        serviceRequestEvent("setEligibleVoters", requestString)
        val response = service.setEligibleVoters(accessToken, electionName, userNames)
        serviceResponseEvent("setEligibleVoters", requestString, response.toKotlinString())
        return response
    }

    override fun isEligible(accessToken: AccessToken, userName: String, electionName: String): Boolean {
        val requestString =
            "${accessToken.toKotlinString()}, ${userName.toKotlinString()}, ${electionName.toKotlinString()}"
        serviceRequestEvent("isEligible", requestString)
        val response = service.isEligible(accessToken, userName, electionName)
        serviceResponseEvent("isEligible", requestString, response.toKotlinString())
        return response
    }

    override fun getBallot(accessToken: AccessToken, voterName: String, electionName: String): BallotSummary? {
        val requestString =
            "${accessToken.toKotlinString()}, ${voterName.toKotlinString()}, ${electionName.toKotlinString()}"
        serviceRequestEvent("getBallot", requestString)
        val response = service.getBallot(accessToken, voterName, electionName)
        serviceResponseEvent("getBallot", requestString, response.toKotlinString())
        return response
    }
}
