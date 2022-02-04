package com.seanshubin.condorcet.backend.console

import com.seanshubin.condorcet.backend.crypto.SecureRandomIdGenerator
import com.seanshubin.condorcet.backend.crypto.UniqueIdGenerator
import com.seanshubin.condorcet.backend.dependencies.Integration
import com.seanshubin.condorcet.backend.genericdb.GenericTable
import com.seanshubin.condorcet.backend.http.RequestValue
import com.seanshubin.condorcet.backend.http.ResponseValue
import com.seanshubin.condorcet.backend.server.LoggingNotificationsFactory
import com.seanshubin.condorcet.backend.server.Notifications
import java.nio.file.Path
import java.nio.file.Paths
import java.sql.SQLException
import java.time.Clock
import kotlin.random.Random

class ConsoleIntegration : Integration {
    private val logDir: Path = Paths.get("out", "log")
    private val loggingNotificationsFactory: LoggingNotificationsFactory = LoggingNotificationsFactory()
    override val createLoggingNotifications: (Path) -> Notifications = loggingNotificationsFactory::createNotifications
    private val notifications: Notifications = createLoggingNotifications(logDir)
    override val user: String = "root"
    override val eventSchemaName: String = "condorcet_development_event_can_be_purged"
    override val stateSchemaName: String = "condorcet_development_state_can_be_purged"
    override val rootDatabaseEvent: (String) -> Unit = notifications::rootDatabaseEvent
    override val eventDatabaseEvent: (String) -> Unit = notifications::eventDatabaseEvent
    override val stateDatabaseEvent: (String) -> Unit = notifications::stateDatabaseEvent
    override val eventTableEvent: (GenericTable) -> Unit = notifications::eventTableEvent
    override val stateTableEvent: (GenericTable) -> Unit = notifications::stateTableEvent
    override val httpRequestEvent: (RequestValue) -> Unit = notifications::httpRequestEvent
    override val httpResponseEvent: (ResponseValue) -> Unit = notifications::httpResponseEvent
    override val serviceRequestEvent: (String, String) -> Unit = notifications::serviceRequestEvent
    override val serviceResponseEvent: (String, String, String) -> Unit = notifications::serviceResponseEvent
    override val topLevelException: (Throwable) -> Unit = notifications::topLevelException
    override val sqlException: (String, String, SQLException) -> Unit = notifications::sqlException
    override val uniqueIdGenerator: UniqueIdGenerator = SecureRandomIdGenerator()
    override val clock: Clock = Clock.systemUTC()
    override val secretsDir: Path = Paths.get("secrets")
    override val random: Random = Random.Default
}