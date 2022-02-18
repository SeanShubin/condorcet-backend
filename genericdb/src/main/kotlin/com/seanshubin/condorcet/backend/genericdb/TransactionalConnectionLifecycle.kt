package com.seanshubin.condorcet.backend.genericdb

import java.sql.DriverManager
import java.sql.SQLException

class TransactionalConnectionLifecycle(
    private val databaseConfiguration: DatabaseConfiguration,
    private val sqlEvent: (String) -> Unit,
    private val sqlException: (String, String, SQLException) -> Unit
) : Lifecycle<ConnectionWrapper> {
    override fun <U> withValue(f: (ConnectionWrapper) -> U): U {
        val host = databaseConfiguration.lookupHost()
        val user = databaseConfiguration.lookupUser()
        val password = databaseConfiguration.lookupPassword()
        val port = databaseConfiguration.lookupPort()
        val schemaName = databaseConfiguration.lookupSchemaName()
        val url = "jdbc:mysql://$host:$port/$schemaName?serverTimezone=UTC"
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
