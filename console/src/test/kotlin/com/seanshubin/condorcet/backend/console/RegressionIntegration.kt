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
import com.seanshubin.condorcet.backend.logger.LoggerFactory
import com.seanshubin.condorcet.backend.mail.MailService
import com.seanshubin.condorcet.backend.mail.SendMailCommand
import com.seanshubin.condorcet.backend.server.Configuration
import com.seanshubin.condorcet.backend.server.JsonConfiguration
import com.seanshubin.condorcet.backend.server.Notifications
import com.seanshubin.condorcet.backend.string.util.ByteArrayFormat
import com.seanshubin.condorcet.backend.string.util.ByteArrayFormatServiceLocator
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.nio.file.Paths
import java.sql.SQLException
import java.time.Clock
import kotlin.random.Random

class RegressionIntegration(phase: Phase) : Integration {
    val regressionSnapshotDir =
        Paths.get("src", "test", "resources", "com", "seanshubin", "condorcet", "backend", "console")
    override val backupFilePath: Path = regressionSnapshotDir.resolve("backup.txt")
    val clockPath = regressionSnapshotDir.resolve("deterministic-clock.txt")
    val realClock = Clock.systemUTC()
    val byteArrayFormat:ByteArrayFormat = ByteArrayFormatServiceLocator.byteArrayFormat
    val realUniqueIdGenerator: UniqueIdGenerator = SecureRandomIdGenerator(byteArrayFormat)
    val uniqueIdGeneratorPath = regressionSnapshotDir.resolve("deterministic-unique-id.txt")
    val randomPath = regressionSnapshotDir.resolve("deterministic-random.txt")
    val charset: Charset = StandardCharsets.UTF_8
    val files: FilesContract = FilesDelegate
    val loggerFactory = LoggerFactory.instanceDefaultZone
    val regressionData: RegressionData = RegressionDataImpl(
        loggerFactory,
        regressionSnapshotDir,
        phase,
        files,
        charset
    )
    private val regressionNotifications: Notifications = regressionData.createNotifications(regressionSnapshotDir)
    override val createLoggingNotifications: (Path) -> Notifications = regressionData::createNotifications
    override val rootDatabaseEvent: (String) -> Unit = regressionNotifications::rootDatabaseEvent
    override val eventDatabaseEvent: (String) -> Unit = regressionNotifications::eventDatabaseEvent
    override val stateDatabaseEvent: (String) -> Unit = regressionNotifications::stateDatabaseEvent
    override val httpRequestEvent: (RequestValue) -> Unit = regressionNotifications::httpRequestEvent
    override val httpResponseEvent: (ResponseValue) -> Unit = regressionNotifications::httpResponseEvent
    override val serviceRequestEvent: (String, String) -> Unit = regressionNotifications::serviceRequestEvent
    override val serviceResponseEvent: (String, String, String) -> Unit = regressionNotifications::serviceResponseEvent
    override val eventTableEvent: (GenericTable) -> Unit = regressionNotifications::eventTableEvent
    override val stateTableEvent: (GenericTable) -> Unit = regressionNotifications::stateTableEvent
    override val topLevelException: (Throwable) -> Unit = regressionNotifications::topLevelException
    override val sqlException: (String, String, SQLException) -> Unit = regressionNotifications::sqlException
    override val sendMailEvent: (SendMailCommand) -> Unit = regressionNotifications::sendMailEvent
    override val uniqueIdGenerator: UniqueIdGenerator =
        RememberingUuidGenerator(realUniqueIdGenerator, uniqueIdGeneratorPath)
    override val clock: Clock = RememberingClock(realClock, clockPath)
    private val configurationPath: Path = regressionSnapshotDir.resolve("configuration.json")
    private val secretsConfigurationPath: Path = regressionSnapshotDir.resolve("secret-configuration.json")
    private val configurationFactory: ConfigurationFactory = JsonFileConfigurationFactory(files, configurationPath)
    private val secretsConfigurationFactory: ConfigurationFactory =
        JsonFileConfigurationFactory(files, secretsConfigurationPath)
    override val configuration: Configuration = JsonConfiguration(configurationFactory, secretsConfigurationFactory)
    override val whereKeysAreStored: Path = regressionSnapshotDir
    private val backingRandom = Random.Default
    override val random: Random = RememberingRandom(backingRandom, randomPath)
    override val mailService: MailService = MailServiceStub(sendMailEvent)
}
