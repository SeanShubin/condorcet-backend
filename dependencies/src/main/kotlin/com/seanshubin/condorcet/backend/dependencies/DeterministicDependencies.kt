package com.seanshubin.condorcet.backend.dependencies

import com.seanshubin.condorcet.backend.contract.FilesContract
import com.seanshubin.condorcet.backend.contract.FilesDelegate
import com.seanshubin.condorcet.backend.crypto.OneWayHash
import com.seanshubin.condorcet.backend.crypto.PasswordUtil
import com.seanshubin.condorcet.backend.crypto.Sha256Hash
import com.seanshubin.condorcet.backend.crypto.UniqueIdGenerator
import com.seanshubin.condorcet.backend.database.*
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
import com.seanshubin.condorcet.backend.service.ApiService
import com.seanshubin.condorcet.backend.service.Lifecycles
import com.seanshubin.condorcet.backend.service.Service
import com.seanshubin.condorcet.backend.service.ServiceLifecycles
import com.seanshubin.condorcet.backend.service.http.ServiceCommandParser
import com.seanshubin.condorcet.backend.service.http.ServiceCommandParserImpl
import org.eclipse.jetty.server.Handler
import org.eclipse.jetty.server.Server
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.time.Clock

class DeterministicDependencies(
    integration: Integration
) {
    private val host: String = integration.host
    private val user: String = integration.user
    private val password: String = integration.password
    private val eventSchemaName: String = integration.eventSchemaName
    private val stateSchemaName: String = integration.stateSchemaName

    private val eventDatabaseEvent: (String) -> Unit = integration.eventDatabaseEvent
    private val stateDatabaseEvent: (String) -> Unit = integration.stateDatabaseEvent
    private val eventTableEvent: (GenericTable) -> Unit = integration.eventTableEvent
    private val stateTableEvent: (GenericTable) -> Unit = integration.stateTableEvent
    private val requestEvent: (RequestValue) -> Unit = integration.requestEvent
    private val responseEvent: (ResponseValue) -> Unit = integration.responseEvent
    private val eventConnectionLifecycle: Lifecycle<ConnectionWrapper> =
        ConnectionLifecycle(host, user, password, eventDatabaseEvent, EventSchema)
    private val stateConnectionLifecycle: Lifecycle<ConnectionWrapper> =
        ConnectionLifecycle(host, user, password, stateDatabaseEvent, StateSchema)
    val lifecycles: Lifecycles = ServiceLifecycles(
        eventConnectionLifecycle = eventConnectionLifecycle,
        stateConnectionLifecycle = stateConnectionLifecycle
    )
    private val queryLoader: QueryLoader = QueryLoaderFromResource()
    private val port: Int = 8080
    private val server: Server = Server(port)
    private val serverContract: ServerContract = JettyServer(server)
    private val oneWayHash: OneWayHash = Sha256Hash()
    private val uniqueIdGenerator: UniqueIdGenerator = integration.uniqueIdGenerator
    private val passwordUtil: PasswordUtil = PasswordUtil(uniqueIdGenerator, oneWayHash)
    private val eventGenericDatabase: GenericDatabase = GenericDatabaseImpl(
        eventConnectionLifecycle::getValue,
        queryLoader
    )
    private val eventDbQueries: EventDbQueries = EventDbQueriesImpl(
        eventGenericDatabase
    )
    private val stateGenericDatabase: GenericDatabase = GenericDatabaseImpl(
        stateConnectionLifecycle::getValue,
        queryLoader
    )
    private val stateDbCommands: StateDbCommands = StateDbCommandsImpl(stateGenericDatabase)
    private val dbEventParser: DbEventParser = DbEventParserImpl()
    private val clock: Clock = integration.clock
    private val stateDbQueries: StateDbQueries = StateDbQueriesImpl(stateGenericDatabase)
    val synchronizer: Synchronizer = SynchronizerImpl(
        eventDbQueries,
        stateDbQueries,
        stateDbCommands,
        dbEventParser
    )
    val eventDbCommands: EventDbCommands = EventDbCommandsImpl(
        eventGenericDatabase,
        synchronizer,
        clock
    )
    private val syncDbCommands: StateDbCommands = SyncDbCommands(eventDbCommands)
    private val nop: () -> Unit = {}
    private val eventInitializer: Initializer =
        SchemaInitializer(
            lifecycles::eventConnection,
            eventSchemaName,
            EventSchema,
            queryLoader,
            nop,
            eventTableEvent
        )
    private val stateInitializer: Initializer =
        SchemaInitializer(
            lifecycles::stateConnection,
            stateSchemaName,
            StateSchema,
            queryLoader,
            synchronizer::synchronize,
            stateTableEvent
        )
    val initializer: Initializer = CompositeInitializer(eventInitializer, stateInitializer)
    val service: Service = ApiService(
        passwordUtil,
        syncDbCommands,
        stateDbQueries,
        stateConnectionLifecycle::getValue,
        eventConnectionLifecycle::getValue
    )
    private val serviceCommandParser: ServiceCommandParser = ServiceCommandParserImpl()
    private val files: FilesContract = FilesDelegate
    private val charset: Charset = StandardCharsets.UTF_8
    private val whereKeysAreStored: Path = integration.whereKeysAreStored
    private val algorithmFactory: AlgorithmFactory = AlgorithmFactoryImpl(files, charset, whereKeysAreStored)
    private val cipher: Cipher = CipherImpl(algorithmFactory)
    val handler: Handler = ApiHandler(
        serviceCommandParser,
        service,
        cipher,
        requestEvent,
        responseEvent
    )
    val runner: Runnable = ServerRunner(lifecycles, initializer, serverContract, handler)
}