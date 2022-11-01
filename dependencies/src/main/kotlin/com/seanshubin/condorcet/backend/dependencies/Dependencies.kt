package com.seanshubin.condorcet.backend.dependencies

import com.seanshubin.condorcet.backend.contract.FilesContract
import com.seanshubin.condorcet.backend.contract.FilesDelegate
import com.seanshubin.condorcet.backend.database.ImmutableDbOperations
import com.seanshubin.condorcet.backend.genericdb.*
import com.seanshubin.condorcet.backend.global.constants.Constants
import com.seanshubin.condorcet.backend.http.RequestValue
import com.seanshubin.condorcet.backend.http.ResponseValue
import com.seanshubin.condorcet.backend.jwt.*
import com.seanshubin.condorcet.backend.mail.MailService
import com.seanshubin.condorcet.backend.server.*
import com.seanshubin.condorcet.backend.service.AccessToken
import com.seanshubin.condorcet.backend.service.Service
import com.seanshubin.condorcet.backend.service.ServiceDelegateToLifecycle
import com.seanshubin.condorcet.backend.service.http.ServiceCommandParser
import com.seanshubin.condorcet.backend.service.http.ServiceCommandParserImpl
import com.seanshubin.condorcet.backend.service.http.TokenUtil
import com.seanshubin.condorcet.backend.service.http.TokenUtilImpl
import com.seanshubin.condorcet.backend.string.util.ByteArrayFormat
import com.seanshubin.condorcet.backend.string.util.ByteArrayFormatServiceLocator
import org.eclipse.jetty.server.Handler
import org.eclipse.jetty.server.Server
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.sql.SQLException
import java.time.Clock
import java.time.Duration

class Dependencies(
    args: Array<String>,
    integration: Integration
) {
    private val clock: Clock = integration.clock
    private val backupFilePath: Path = integration.backupFilePath
    private val configuration: Configuration = integration.configuration
    private val files: FilesContract = FilesDelegate
    private val rootDatabaseEvent: (String) -> Unit = integration.rootDatabaseEvent
    private val eventDatabaseEvent: (String) -> Unit = integration.eventDatabaseEvent
    private val stateDatabaseEvent: (String) -> Unit = integration.stateDatabaseEvent
    private val sqlException: (String, String, SQLException) -> Unit = integration.sqlException
    private val requestEvent: (RequestValue) -> Unit = integration.httpRequestEvent
    private val responseEvent: (ResponseValue) -> Unit = integration.httpResponseEvent
    private val topLevelException: (Throwable) -> Unit = integration.topLevelException
    private val rootConnectionLifecycle: Lifecycle<ConnectionWrapper> =
        ConnectionLifecycle(configuration.rootDatabase, rootDatabaseEvent, sqlException)
    private val eventConnectionLifecycle: Lifecycle<ConnectionWrapper> =
        TransactionalConnectionLifecycle(
            configuration.immutableDatabase,
            eventDatabaseEvent,
            sqlException
        )
    private val stateConnectionLifecycle: Lifecycle<ConnectionWrapper> =
        TransactionalConnectionLifecycle(
            configuration.mutableDatabase,
            stateDatabaseEvent,
            sqlException
        )
    private val lookupServerPort: () -> Int = configuration.server.lookupPort
    private val server: Server = Server(lookupServerPort())
    private val serverContract: ServerContract = JettyServer(server)
    private val charset: Charset = StandardCharsets.UTF_8
    private val whereKeysAreStored: Path = integration.whereKeysAreStored
    private val byteArrayFormat:ByteArrayFormat = ByteArrayFormatServiceLocator.byteArrayFormat
    private val keyStore: KeyStore = KeyStoreImpl(files, charset, whereKeysAreStored, byteArrayFormat)
    private val algorithmFactory: AlgorithmFactory = AlgorithmFactoryImpl(keyStore)
    private val cipher: Cipher = CipherImpl(algorithmFactory, clock)
    private val tokenUtil: TokenUtil = TokenUtilImpl(cipher)
    private val createUpdatePasswordLink: (AccessToken, String) -> String = tokenUtil::createUpdatePasswordLink
    private val createService: (ConnectionWrapper, ConnectionWrapper, MailService) -> Service =
        { eventConnection, stateConnection, mailService ->
            ServiceDependencies(
                integration,
                eventConnection,
                stateConnection,
                mailService, configuration,
                Constants.emailAccessTokenDuration,
                createUpdatePasswordLink
            ).service
        }
    private val emailAccessTokenExpire: Duration = Constants.emailAccessTokenDuration
    private val createImmutableDbOperations: (ConnectionWrapper, ConnectionWrapper) -> ImmutableDbOperations =
        { eventConnection, stateConnection ->
            val serviceDependencies = ServiceDependencies(
                integration,
                eventConnection,
                stateConnection,
                mailService,
                configuration,
                emailAccessTokenExpire,
                createUpdatePasswordLink
            )
            val immutableDbQueries = serviceDependencies.immutableDbQueries
            val immutableDbCommands = serviceDependencies.immutableDbCommands
            ImmutableDbOperations(immutableDbQueries, immutableDbCommands)
        }
    private val createSchemaCreator: (ConnectionWrapper) -> SchemaCreator = { connection ->
        InitializerDependencies(
            integration,
            connection,
            configuration.immutableDatabase.lookupSchemaName,
            configuration.mutableDatabase.lookupSchemaName
        ).schemaCreator
    }
    val schemaCreator: SchemaCreator =
        SchemaCreatorDelegateToLifecycle(createSchemaCreator, rootConnectionLifecycle)
    private val mailService: MailService = integration.mailService
    val service: Service = ServiceDelegateToLifecycle(
        createService,
        eventConnectionLifecycle,
        stateConnectionLifecycle,
        mailService
    )
    private val serviceCommandParser: ServiceCommandParser = ServiceCommandParserImpl()
    val handler: Handler = ApiHandler(
        schemaCreator,
        serviceCommandParser,
        service,
        tokenUtil,
        requestEvent,
        responseEvent,
        topLevelException
    )
    val serverRunner: Runnable = ServerRunner(serverContract, handler)
    val backupRunner: Runnable = BackupRunner(
        backupFilePath,
        charset,
        files,
        eventConnectionLifecycle,
        stateConnectionLifecycle,
        createImmutableDbOperations
    )
    val restoreRunner: Runnable = RestoreRunner(
        backupFilePath,
        charset,
        files,
        rootConnectionLifecycle,
        eventConnectionLifecycle,
        stateConnectionLifecycle,
        createImmutableDbOperations,
        createSchemaCreator
    )
    val runner: Runnable = DispatchRunner(args, configuration, serverRunner, backupRunner, restoreRunner)
}