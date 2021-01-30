package com.seanshubin.condorcet.backend.database.util

import java.sql.DriverManager

class ConnectionLifecycle(
    private val host: String,
    private val user: String,
    private val password: String,
    private val sqlEvent: (String) -> Unit
) : Lifecycle<ConnectionWrapper> {
    private lateinit var connectionWrapper: ConnectionWrapper
    override fun open() {
        val url = "jdbc:mysql://$host?serverTimezone=UTC"
        val connection = DriverManager.getConnection(url, user, password)
        connectionWrapper = ConnectionWrapper(connection, sqlEvent)
    }

    override fun getValue(): ConnectionWrapper {
        return connectionWrapper
    }

    override fun close() {
        connectionWrapper.close()
    }
}
