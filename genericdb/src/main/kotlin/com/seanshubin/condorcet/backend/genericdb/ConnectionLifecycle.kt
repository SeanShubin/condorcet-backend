package com.seanshubin.condorcet.backend.genericdb

import java.sql.DriverManager
import java.sql.SQLException

class ConnectionLifecycle(
    private val host: String,
    private val user: String,
    private val password: String,
    private val sqlEvent: (String) -> Unit,
    private val sqlException:(String, String, SQLException) -> Unit
) : Lifecycle<ConnectionWrapper> {
    override fun <U> withValue(f: (ConnectionWrapper) -> U): U {
        val url = "jdbc:mysql://$host?serverTimezone=UTC"
        return DriverManager.getConnection(url, user, password).use { connection ->
            val connectionWrapper = ConnectionWrapper(connection, sqlEvent, sqlException)
            f(connectionWrapper)
        }
    }
}
