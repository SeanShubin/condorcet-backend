package com.seanshubin.condorcet.backend.console

import com.seanshubin.condorcet.backend.crypto.UniqueIdGenerator
import com.seanshubin.condorcet.backend.crypto.Uuid4
import com.seanshubin.condorcet.backend.dependencies.Integration
import com.seanshubin.condorcet.backend.genericdb.GenericTable
import com.seanshubin.condorcet.backend.http.RequestValue
import com.seanshubin.condorcet.backend.http.ResponseValue
import com.seanshubin.condorcet.backend.server.LoggingNotifications
import com.seanshubin.condorcet.backend.server.Notifications
import java.nio.file.Path
import java.nio.file.Paths
import java.time.Clock

class ConsoleIntegration : Integration {
    private val logDir: Path = Paths.get("out", "log")
    private val notifications: Notifications = LoggingNotifications(logDir)

    override val host: String = "localhost"
    override val user: String = "root"
    override val password: String = "insecure"
    override val eventSchemaName: String = "condorcet_development_event"
    override val stateSchemaName: String = "condorcet_development_state"
    override val rootDatabaseEvent: (String) -> Unit = notifications::rootDatabaseEvent
    override val eventDatabaseEvent: (String) -> Unit = notifications::eventDatabaseEvent
    override val stateDatabaseEvent: (String) -> Unit = notifications::stateDatabaseEvent
    override val eventTableEvent: (GenericTable) -> Unit = notifications::eventTableEvent
    override val stateTableEvent: (GenericTable) -> Unit = notifications::stateTableEvent
    override val requestEvent: (RequestValue) -> Unit = notifications::requestEvent
    override val responseEvent: (ResponseValue) -> Unit = notifications::responseEvent
    override val uniqueIdGenerator: UniqueIdGenerator = Uuid4()
    override val clock: Clock = Clock.systemUTC()
    override val whereKeysAreStored: Path = Paths.get("keys")
}