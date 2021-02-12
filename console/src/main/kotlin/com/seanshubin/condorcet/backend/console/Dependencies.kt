package com.seanshubin.condorcet.backend.console

import com.seanshubin.condorcet.backend.contract.FilesContract
import com.seanshubin.condorcet.backend.contract.FilesDelegate
import com.seanshubin.condorcet.backend.crypto.*
import com.seanshubin.condorcet.backend.database.*
import com.seanshubin.condorcet.backend.genericdb.*
import com.seanshubin.condorcet.backend.http.RequestValue
import com.seanshubin.condorcet.backend.http.ResponseValue
import com.seanshubin.condorcet.backend.jwt.AlgorithmFactory
import com.seanshubin.condorcet.backend.jwt.AlgorithmFactoryImpl
import com.seanshubin.condorcet.backend.jwt.Cipher
import com.seanshubin.condorcet.backend.jwt.CipherImpl
import com.seanshubin.condorcet.backend.server.*
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
import java.nio.file.Paths
import java.time.Clock

class Dependencies {
    private val host: String = "localhost"
    private val user: String = "root"
    private val password: String = "insecure"
    private val logDir: Path = Paths.get("out", "log")
    private val notifications: Notifications = LoggingNotifications(logDir)
    private val databaseEvent: (String) -> Unit = notifications::databaseEvent
    private val requestEvent: (RequestValue) -> Unit = notifications::requestEvent
    private val responseEvent: (ResponseValue) -> Unit = notifications::responseEvent
    private val eventConnectionLifecycle: Lifecycle<ConnectionWrapper> =
        ConnectionLifecycle(host, user, password, databaseEvent)
    private val stateConnectionLifecycle: Lifecycle<ConnectionWrapper> =
        ConnectionLifecycle(host, user, password, databaseEvent)
    private val lifecycles: Lifecycles = ServiceLifecycles(
        eventConnectionLifecycle = eventConnectionLifecycle,
        stateConnectionLifecycle = stateConnectionLifecycle
    )
    private val queryLoader: QueryLoader = QueryLoaderFromResource()
    private val port: Int = 8080
    private val server: Server = Server(port)
    private val serverContract: ServerContract = JettyServer(server)
    private val uniqueIdGenerator: UniqueIdGenerator = Uuid4()
    private val oneWayHash: OneWayHash = Sha256Hash()
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
    private val clock: Clock = Clock.systemUTC()
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
        SchemaInitializer(lifecycles::eventConnection, EventSchema, queryLoader, nop)
    private val stateInitializer: Initializer =
        SchemaInitializer(lifecycles::stateConnection, StateSchema, queryLoader, synchronizer::synchronize)
    private val initializer: Initializer = CompositeInitializer(eventInitializer, stateInitializer)
    private val service: Service = ApiService(passwordUtil, syncDbCommands, stateDbQueries)
    private val serviceCommandParser: ServiceCommandParser = ServiceCommandParserImpl()
    private val files: FilesContract = FilesDelegate
    private val charset: Charset = StandardCharsets.UTF_8
    private val keyBasePath: Path = Paths.get("keys")
    private val algorithmFactory: AlgorithmFactory = AlgorithmFactoryImpl(files, charset, keyBasePath)
    private val cipher: Cipher = CipherImpl(algorithmFactory)
    private val handler: Handler = ApiHandler(
        serviceCommandParser,
        service,
        cipher,
        requestEvent,
        responseEvent
    )
    val runner: Runnable = ServerRunner(lifecycles, initializer, serverContract, handler)
}
