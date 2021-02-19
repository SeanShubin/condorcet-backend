package com.seanshubin.condorcet.backend.genericdb

import java.sql.DriverManager

class TransactionalConnectionLifecycle(
    private val host: String,
    private val user: String,
    private val password: String,
    private val sqlEvent: (String) -> Unit,
    private val schemaName: String
) : Lifecycle<ConnectionWrapper> {
    override fun <U> withValue(f: (ConnectionWrapper) -> U): U {
        val url = "jdbc:mysql://$host/$schemaName?serverTimezone=UTC"
        return DriverManager.getConnection(url, user, password).use { connection ->
            connection.autoCommit = false
            val connectionWrapper = ConnectionWrapper(connection, sqlEvent)
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
