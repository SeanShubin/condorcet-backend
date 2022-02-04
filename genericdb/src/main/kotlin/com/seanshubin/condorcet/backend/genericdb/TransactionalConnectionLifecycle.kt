package com.seanshubin.condorcet.backend.genericdb

import java.sql.DriverManager
import java.sql.SQLException

class TransactionalConnectionLifecycle(
    private val lookupHost: () -> String,
    private val user: String,
    private val lookupPassword: () -> String,
    private val schemaName: String,
    private val sqlEvent: (String) -> Unit,
    private val sqlException: (String, String, SQLException) -> Unit
) : Lifecycle<ConnectionWrapper> {
    override fun <U> withValue(f: (ConnectionWrapper) -> U): U {
        val host = lookupHost()
        val password = lookupPassword()
        val url = "jdbc:mysql://$host/$schemaName?serverTimezone=UTC"
        Class.forName("com.mysql.cj.jdbc.Driver")
        return DriverManager.getConnection(url, user, password).use { connection ->
            connection.autoCommit = false
            val connectionWrapper = ConnectionWrapper(connection, sqlEvent, sqlException)
            try {
                val result = f(connectionWrapper)
                connection.commit()
                result
            } finally {
                connection.rollback()
            }
        }
    }
}
