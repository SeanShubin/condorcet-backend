package com.seanshubin.condorcet.backend.dependencies

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

class Dependencies(
    integration: Integration
) {
    private val host: String = integration.host
    private val user: String = integration.user
    private val password: String = integration.password
    private val eventSchemaName: String = integration.eventSchemaName
    private val stateSchemaName: String = integration.stateSchemaName
    private val rootDatabaseEvent: (String) -> Unit = integration.rootDatabaseEvent
    private val eventDatabaseEvent: (String) -> Unit = integration.eventDatabaseEvent
    private val stateDatabaseEvent: (String) -> Unit = integration.stateDatabaseEvent
    private val requestEvent: (RequestValue) -> Unit = integration.requestEvent
    private val responseEvent: (ResponseValue) -> Unit = integration.responseEvent
    private val rootConnectionLifecycle: Lifecycle<ConnectionWrapper> =
        ConnectionLifecycle(host, user, password, rootDatabaseEvent)
    private val eventConnectionLifecycle: Lifecycle<ConnectionWrapper> =
        TransactionalConnectionLifecycle(host, user, password, eventDatabaseEvent, eventSchemaName)
    private val stateConnectionLifecycle: Lifecycle<ConnectionWrapper> =
        TransactionalConnectionLifecycle(host, user, password, stateDatabaseEvent, stateSchemaName)
    private val port: Int = 8080
    private val server: Server = Server(port)
    private val serverContract: ServerContract = JettyServer(server)
    private val createService: (ConnectionWrapper, ConnectionWrapper) -> Service = { eventConnection, stateConnection ->
        ServiceDependencies(integration, eventConnection, stateConnection).service
    }
    private val createSchemaCreator: (ConnectionWrapper) -> SchemaCreator = { connection ->
        InitializerDependencies(integration, connection).schemaCreator
    }
    val schemaCreator: SchemaCreator =
        SchemaSchemaCreatorDelegateToLifecycle(createSchemaCreator, rootConnectionLifecycle)
    val service: Service = ServiceDelegateToLifecycle(
        createService,
        eventConnectionLifecycle,
        stateConnectionLifecycle
    )
    private val serviceCommandParser: ServiceCommandParser = ServiceCommandParserImpl()
    private val files: FilesContract = FilesDelegate
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
        responseEvent
    )
    val runner: Runnable = ServerRunner(serverContract, handler)
}