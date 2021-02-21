package com.seanshubin.condorcet.backend.service

import com.seanshubin.condorcet.backend.domain.Role
import com.seanshubin.condorcet.backend.domain.TableData
import com.seanshubin.condorcet.backend.domain.UserNameRole
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

    override fun register(rawName: String, email: String, password: String): Tokens =
        withService { it.register(rawName, email, password) }

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

    override fun listTables(accessToken: AccessToken): List<String> =
        withService { it.listTables(accessToken) }

    override fun tableData(accessToken: AccessToken, name: String): TableData =
        withService { it.tableData(accessToken, name) }

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

    private fun <T> withService(f: (Service) -> T): T =
        eventConnectionLifecycle.withValue { eventConnection ->
            stateConnectionLifecycle.withValue { stateConnection ->
                val service = createService(eventConnection, stateConnection)
                f(service)
            }
        }
}
