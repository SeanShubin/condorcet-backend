package com.seanshubin.condorcet.backend.genericdb

import java.sql.DriverManager
import java.sql.SQLException

class ConnectionLifecycle(
    private val lookupHost: () -> String,
    private val lookupUser: () -> String,
    private val lookupPassword: () -> String,
    private val lookupPort: () -> Int,
    private val sqlEvent: (String) -> Unit,
    private val sqlException:(String, String, SQLException) -> Unit
) : Lifecycle<ConnectionWrapper> {
    override fun <U> withValue(f: (ConnectionWrapper) -> U): U {
        val host = lookupHost()
        val user = lookupUser()
        val password = lookupPassword()
        val port = lookupPort()
        val url = "jdbc:mysql://$host:$port?serverTimezone=UTC"
        return DriverManager.getConnection(url, user, password).use { connection ->
            val connectionWrapper = ConnectionWrapper(connection, sqlEvent, sqlException)
            f(connectionWrapper)
        }
    }
}
