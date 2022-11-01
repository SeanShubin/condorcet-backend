package com.seanshubin.condorcet.backend.console

import com.seanshubin.condorcet.backend.configuration.util.ConfigurationFactory
import com.seanshubin.condorcet.backend.configuration.util.JsonFileConfigurationFactory
import com.seanshubin.condorcet.backend.contract.FilesContract
import com.seanshubin.condorcet.backend.contract.FilesDelegate
import com.seanshubin.condorcet.backend.crypto.SecureRandomIdGenerator
import com.seanshubin.condorcet.backend.crypto.UniqueIdGenerator
import com.seanshubin.condorcet.backend.dependencies.Integration
import com.seanshubin.condorcet.backend.genericdb.GenericTable
import com.seanshubin.condorcet.backend.http.RequestValue
import com.seanshubin.condorcet.backend.http.ResponseValue
import com.seanshubin.condorcet.backend.mail.MailConfiguration
import com.seanshubin.condorcet.backend.mail.MailService
import com.seanshubin.condorcet.backend.mail.SendMailCommand
import com.seanshubin.condorcet.backend.mail.SmtpMailService
import com.seanshubin.condorcet.backend.server.Configuration
import com.seanshubin.condorcet.backend.server.JsonConfiguration
import com.seanshubin.condorcet.backend.server.LoggingNotificationsFactory
import com.seanshubin.condorcet.backend.server.Notifications
import com.seanshubin.condorcet.backend.string.util.ByteArrayFormat
import com.seanshubin.condorcet.backend.string.util.ByteArrayFormatServiceLocator
import java.nio.file.Path
import java.nio.file.Paths
import java.sql.SQLException
import java.time.Clock
import kotlin.random.Random

class ConsoleIntegration : Integration {
    override val backupFilePath: Path = Paths.get("backup.txt")
    private val logDir: Path = Paths.get("out", "log")
    private val loggingNotificationsFactory: LoggingNotificationsFactory = LoggingNotificationsFactory()
    override val createLoggingNotifications: (Path) -> Notifications = loggingNotificationsFactory::createNotifications
    private val notifications: Notifications = createLoggingNotifications(logDir)
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
    override val sendMailEvent: (SendMailCommand) -> Unit = notifications::sendMailEvent
    private val byteArrayFormat:ByteArrayFormat = ByteArrayFormatServiceLocator.byteArrayFormat
    override val uniqueIdGenerator: UniqueIdGenerator = SecureRandomIdGenerator(byteArrayFormat)
    override val clock: Clock = Clock.systemUTC()
    private val configurationDir = Paths.get("local-config")
    private val secretsDir: Path = configurationDir.resolve("secrets")
    private val configurationPath: Path = configurationDir.resolve("configuration.json")
    private val secretsConfigurationPath: Path = secretsDir.resolve("secret-configuration.json")
    private val files: FilesContract = FilesDelegate
    private val configurationFactory: ConfigurationFactory = JsonFileConfigurationFactory(files, configurationPath)
    private val secretsConfigurationFactory: ConfigurationFactory =
        JsonFileConfigurationFactory(files, secretsConfigurationPath)
    override val configuration: Configuration =
        JsonConfiguration(configurationFactory, secretsConfigurationFactory)
    override val whereKeysAreStored: Path = secretsDir
    override val random: Random = Random.Default
    private val mailConfiguration: MailConfiguration = configuration.mail
    override val mailService: MailService = SmtpMailService(mailConfiguration, sendMailEvent)
}
