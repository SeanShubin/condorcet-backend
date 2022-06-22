package com.seanshubin.condorcet.backend.genericdb

import java.sql.DriverManager
import java.sql.SQLException

class ConnectionLifecycle(
    private val databaseConfiguration: DatabaseConfiguration,
    private val sqlEvent: (String) -> Unit,
    private val sqlException: (String, String, SQLException) -> Unit
) : Lifecycle<ConnectionWrapper> {
    override fun <U> withValue(f: (ConnectionWrapper) -> U): U {
        val host = databaseConfiguration.lookupHost()
        val user = databaseConfiguration.lookupUser()
        val password = databaseConfiguration.lookupPassword()
        val port = databaseConfiguration.lookupPort()
        val url = "jdbc:mysql://$host:$port?serverTimezone=UTC"
        return DriverManager.getConnection(url, user, password).use { connection ->
            val connectionWrapper = ConnectionWrapper(connection, sqlEvent, sqlException)
            f(connectionWrapper)
        }
    }
}
