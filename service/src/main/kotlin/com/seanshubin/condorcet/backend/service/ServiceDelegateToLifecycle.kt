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

    override fun refresh(refreshToken: RefreshToken): Tokens =
        withService { it.refresh(refreshToken) }

    override fun register(name: String, email: String, password: String): Tokens =
        withService { it.register(name, email, password) }

    override fun authenticate(nameOrEmail: String, password: String): Tokens =
        withService { it.authenticate(nameOrEmail, password) }

    override fun setRole(accessToken: AccessToken, name: String, role: Role) {
        withService { it.setRole(accessToken, name, role) }
    }

    override fun removeUser(accessToken: AccessToken, name: String) {
        withService { it.removeUser(accessToken, name) }
    }

    override fun listUsers(accessToken: AccessToken): List<UserNameRole> =
        withService { it.listUsers(accessToken) }

    override fun addElection(accessToken: AccessToken, name: String) {
        withService { it.addElection(accessToken, name) }
    }

    override fun launchElection(accessToken: AccessToken, name: String, allowEdit: Boolean) {
        withService { it.launchElection(accessToken, name, allowEdit) }
    }

    override fun finalizeElection(accessToken: AccessToken, name: String) {
        withService { it.finalizeElection(accessToken, name) }
    }

    override fun updateElection(accessToken: AccessToken, name: String, electionUpdates: ElectionUpdates) {
        withService { it.updateElection(accessToken, name, electionUpdates) }
    }

    override fun getElection(accessToken: AccessToken, name: String): Election =
        withService { it.getElection(accessToken, name) }

    override fun deleteElection(accessToken: AccessToken, name: String) {
        withService { it.deleteElection(accessToken, name) }
    }

    override fun listElections(accessToken: AccessToken): List<Election> =
        withService { it.listElections(accessToken) }

    override fun listTables(accessToken: AccessToken): List<String> =
        withService { it.listTables(accessToken) }

    override fun tableData(accessToken: AccessToken, name: String): TableData =
        withService { it.tableData(accessToken, name) }

    override fun debugTableData(accessToken: AccessToken, name: String): TableData =
        withService { it.debugTableData(accessToken, name) }

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

    override fun listVoterNames(accessToken: AccessToken): List<String> =
        withService { it.listVoterNames(accessToken) }

    private fun <T> withService(f: (Service) -> T): T =
        eventConnectionLifecycle.withValue { eventConnection ->
            stateConnectionLifecycle.withValue { stateConnection ->
                val service = createService(eventConnection, stateConnection)
                f(service)
            }
        }
}
