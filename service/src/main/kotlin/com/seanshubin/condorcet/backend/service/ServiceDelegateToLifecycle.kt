package com.seanshubin.condorcet.backend.service

import com.seanshubin.condorcet.backend.domain.*
import com.seanshubin.condorcet.backend.genericdb.ConnectionWrapper
import com.seanshubin.condorcet.backend.genericdb.Lifecycle

class ServiceDelegateToLifecycle(
    private val createService: (ConnectionWrapper, ConnectionWrapper) -> Service,
    private val eventConnectionLifecycle: Lifecycle<ConnectionWrapper>,
    private val stateConnectionLifecycle: Lifecycle<ConnectionWrapper>
) : Service {
    override fun synchronize() {
        withService { it.synchronize() }
    }

    override fun health(): String =
        withService { it.health() }

    override fun refresh(refreshToken: RefreshToken): Tokens =
        withService { it.refresh(refreshToken) }

    override fun register(userName: String, email: String, password: String): Tokens =
        withService { it.register(userName, email, password) }

    override fun authenticate(nameOrEmail: String, password: String): Tokens =
        withService { it.authenticate(nameOrEmail, password) }

    override fun permissionsForRole(role: Role): List<Permission> =
        withService { it.permissionsForRole(role) }

    override fun setRole(accessToken: AccessToken, userName: String, role: Role) {
        withService { it.setRole(accessToken, userName, role) }
    }

    override fun removeUser(accessToken: AccessToken, userName: String) {
        withService { it.removeUser(accessToken, userName) }
    }

    override fun listUsers(accessToken: AccessToken): List<UserNameRole> =
        withService { it.listUsers(accessToken) }

    override fun addElection(accessToken: AccessToken, electionName: String) {
        withService { it.addElection(accessToken, electionName) }
    }

    override fun launchElection(accessToken: AccessToken, electionName: String, allowEdit: Boolean) {
        withService { it.launchElection(accessToken, electionName, allowEdit) }
    }

    override fun finalizeElection(accessToken: AccessToken, electionName: String) {
        withService { it.finalizeElection(accessToken, electionName) }
    }

    override fun updateElection(accessToken: AccessToken, electionName: String, electionUpdates: ElectionUpdates) {
        withService { it.updateElection(accessToken, electionName, electionUpdates) }
    }

    override fun getElection(accessToken: AccessToken, electionName: String): ElectionDetail =
        withService { it.getElection(accessToken, electionName) }

    override fun deleteElection(accessToken: AccessToken, electionName: String) {
        withService { it.deleteElection(accessToken, electionName) }
    }

    override fun listElections(accessToken: AccessToken): List<ElectionSummary> =
        withService { it.listElections(accessToken) }

    override fun listTables(accessToken: AccessToken): List<String> =
        withService { it.listTables(accessToken) }

    override fun tableData(accessToken: AccessToken, tableName: String): TableData =
        withService { it.tableData(accessToken, tableName) }

    override fun debugTableData(accessToken: AccessToken, tableName: String): TableData =
        withService { it.debugTableData(accessToken, tableName) }

    override fun eventData(accessToken: AccessToken): TableData =
        withService { it.eventData(accessToken) }

    override fun userCount(accessToken: AccessToken): Int =
        withService { it.userCount(accessToken) }

    override fun electionCount(accessToken: AccessToken): Int =
        withService { it.electionCount(accessToken) }

    override fun tableCount(accessToken: AccessToken): Int =
        withService { it.tableCount(accessToken) }

    override fun eventCount(accessToken: AccessToken): Int =
        withService { it.eventCount(accessToken) }

    override fun setCandidates(accessToken: AccessToken, electionName: String, candidateNames: List<String>) {
        withService { it.setCandidates(accessToken, electionName, candidateNames) }
    }

    override fun listCandidates(accessToken: AccessToken, electionName: String): List<String> =
        withService { it.listCandidates(accessToken, electionName) }

    override fun castBallot(
        accessToken: AccessToken,
        voterName: String,
        electionName: String,
        rankings: List<Ranking>
    ) {
        withService { it.castBallot(accessToken, voterName, electionName, rankings) }
    }

    override fun listRankings(accessToken: AccessToken, voterName: String, electionName: String): List<Ranking> =
        withService { it.listRankings(accessToken, voterName, electionName) }

    override fun tally(accessToken: AccessToken, electionName: String): Tally =
        withService { it.tally(accessToken, electionName) }

    override fun listEligibility(accessToken: AccessToken, electionName:String): List<VoterEligibility> =
        withService { it.listEligibility(accessToken, electionName) }

    override fun setEligibleVoters(accessToken: AccessToken, electionName: String, voterNames:List<String>) {
        withService { it.setEligibleVoters(accessToken, electionName, voterNames)}
    }

    override fun isEligible(accessToken: AccessToken, userName: String, electionName: String): Boolean =
        withService { it.isEligible(accessToken, userName, electionName) }

    override fun getBallot(accessToken: AccessToken, voterName: String, electionName: String): BallotSummary? =
        withService { it.getBallot(accessToken, voterName, electionName) }

    private fun <T> withService(f: (Service) -> T): T =
        eventConnectionLifecycle.withValue { eventConnection ->
            stateConnectionLifecycle.withValue { stateConnection ->
                val service = createService(eventConnection, stateConnection)
                f(service)
            }
        }
}
