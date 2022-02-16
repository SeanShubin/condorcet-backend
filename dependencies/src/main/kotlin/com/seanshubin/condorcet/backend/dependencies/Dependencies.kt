package com.seanshubin.condorcet.backend.dependencies

import com.seanshubin.condorcet.backend.configuration.Configuration
import com.seanshubin.condorcet.backend.configuration.JsonFileConfiguration
import com.seanshubin.condorcet.backend.contract.FilesContract
import com.seanshubin.condorcet.backend.contract.FilesDelegate
import com.seanshubin.condorcet.backend.genericdb.*
import com.seanshubin.condorcet.backend.http.RequestValue
import com.seanshubin.condorcet.backend.http.ResponseValue
import com.seanshubin.condorcet.backend.jwt.AlgorithmFactory
import com.seanshubin.condorcet.backend.jwt.AlgorithmFactoryImpl
import com.seanshubin.condorcet.backend.jwt.Cipher
import com.seanshubin.condorcet.backend.jwt.CipherImpl
import com.seanshubin.condorcet.backend.server.ApiHandler
import com.seanshubin.condorcet.backend.server.JettyServer
import com.seanshubin.condorcet.backend.server.ServerContract
import com.seanshubin.condorcet.backend.server.ServerRunner
import com.seanshubin.condorcet.backend.service.Service
import com.seanshubin.condorcet.backend.service.ServiceDelegateToLifecycle
import com.seanshubin.condorcet.backend.service.http.ServiceCommandParser
import com.seanshubin.condorcet.backend.service.http.ServiceCommandParserImpl
import org.eclipse.jetty.server.Handler
import org.eclipse.jetty.server.Server
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.sql.SQLException

class Dependencies(
    integration: Integration
) {
    private val configurationPath: Path = integration.configurationPath
    private val secretsConfigurationPath: Path = integration.secretsConfigurationPath
    private val files: FilesContract = FilesDelegate
    private val configuration: Configuration = JsonFileConfiguration(configurationPath, files)
    private val secretsConfiguration: Configuration = JsonFileConfiguration(secretsConfigurationPath, files)
    private val configurationLookupFunctions: ConfigurationLookupFunctions =
        JsonConfigurationLookupFunctions(configuration, secretsConfiguration)
    private val lookupImmutableSchemaName: () -> String = configurationLookupFunctions.lookupImmutableSchemaName
    private val lookupMutableSchemaName: () -> String = configurationLookupFunctions.lookupMutableSchemaName
    private val rootDatabaseEvent: (String) -> Unit = integration.rootDatabaseEvent
    private val eventDatabaseEvent: (String) -> Unit = integration.eventDatabaseEvent
    private val stateDatabaseEvent: (String) -> Unit = integration.stateDatabaseEvent
    private val sqlException: (String, String, SQLException) -> Unit = integration.sqlException
    private val requestEvent: (RequestValue) -> Unit = integration.httpRequestEvent
    private val responseEvent: (ResponseValue) -> Unit = integration.httpResponseEvent
    private val topLevelException: (Throwable) -> Unit = integration.topLevelException
    private val lookupHost: () -> String = configurationLookupFunctions.lookupDatabaseHost
    private val lookupUser: () -> String = configurationLookupFunctions.lookupDatabaseUser
    private val lookupPassword: () -> String = configurationLookupFunctions.lookupDatabasePassword
    private val lookupDatabasePort: () -> Int = configurationLookupFunctions.lookupDatabasePort
    private val rootConnectionLifecycle: Lifecycle<ConnectionWrapper> =
        ConnectionLifecycle(lookupHost, lookupUser, lookupPassword, lookupDatabasePort, rootDatabaseEvent, sqlException)
    private val eventConnectionLifecycle: Lifecycle<ConnectionWrapper> =
        TransactionalConnectionLifecycle(
            lookupHost,
            lookupUser,
            lookupPassword,
            lookupDatabasePort,
            lookupImmutableSchemaName,
            eventDatabaseEvent,
            sqlException
        )
    private val stateConnectionLifecycle: Lifecycle<ConnectionWrapper> =
        TransactionalConnectionLifecycle(
            lookupHost,
            lookupUser,
            lookupPassword,
            lookupDatabasePort,
            lookupMutableSchemaName,
            stateDatabaseEvent,
            sqlException
        )
    private val lookupServerPort: () -> Int = configurationLookupFunctions.lookupServerPort
    private val server: Server = Server(lookupServerPort())
    private val serverContract: ServerContract = JettyServer(server)
    private val createService: (ConnectionWrapper, ConnectionWrapper) -> Service = { eventConnection, stateConnection ->
        ServiceDependencies(integration, eventConnection, stateConnection).service
    }
    private val createSchemaCreator: (ConnectionWrapper) -> SchemaCreator = { connection ->
        InitializerDependencies(
            integration,
            connection,
            lookupImmutableSchemaName,
            lookupMutableSchemaName).schemaCreator
    }
    val schemaCreator: SchemaCreator =
        SchemaCreatorDelegateToLifecycle(createSchemaCreator, rootConnectionLifecycle)
    val service: Service = ServiceDelegateToLifecycle(
        createService,
        eventConnectionLifecycle,
        stateConnectionLifecycle
    )
    private val serviceCommandParser: ServiceCommandParser = ServiceCommandParserImpl()
    private val charset: Charset = StandardCharsets.UTF_8
    private val whereKeysAreStored: Path = integration.whereKeysAreStored
    private val algorithmFactory: AlgorithmFactory = AlgorithmFactoryImpl(files, charset, whereKeysAreStored)
    private val cipher: Cipher = CipherImpl(algorithmFactory)
    val handler: Handler = ApiHandler(
        schemaCreator,
        serviceCommandParser,
        service,
        cipher,
        requestEvent,
        responseEvent,
        topLevelException
    )
    val runner: Runnable = ServerRunner(serverContract, handler)
}