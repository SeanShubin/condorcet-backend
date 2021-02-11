package com.seanshubin.condorcet.backend.server

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
import com.seanshubin.condorcet.backend.service.*
import com.seanshubin.condorcet.backend.service.http.ServiceCommandParser
import com.seanshubin.condorcet.backend.service.http.ServiceCommandParserImpl
import org.eclipse.jetty.server.Handler
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.nio.file.Paths
import java.time.Clock

class DeterministicDependencies(
    snapshotDir: Path,
    snapshotViews: List<SnapshotView>,
    requestEvents: List<RequestEvent>
) {
    val realClock: Clock = Clock.systemUTC()
    val clockPath = snapshotDir.resolve("deterministic-clock.txt")
    val clock: Clock = RememberingClock(realClock, clockPath)
    val uniqueIdsPath = snapshotDir.resolve("deterministic-unique-ids.txt")
    val serviceRequestParser: ServiceRequestParser = ServiceRequestParserImpl()
    val realUniqueIdGenerator: UniqueIdGenerator = Uuid4()
    val uniqueIdGenerator: UniqueIdGenerator = RememberingUuidGenerator(realUniqueIdGenerator, uniqueIdsPath)
    val oneWayHash: OneWayHash = Sha256Hash()
    val passwordUtil: PasswordUtil = PasswordUtil(uniqueIdGenerator, oneWayHash)
    val queryLoader: QueryLoader = QueryLoaderFromResource()
    val host: String = "localhost"
    val user: String = "root"
    val password: String = "insecure"
    val sqlEventMonitor: SqlMonitor = SqlMonitorInMemory()
    val sqlStateMonitor: SqlMonitor = SqlMonitorInMemory()
    val logSqlEvent: (String) -> Unit = sqlEventMonitor::monitor
    val logSqlState: (String) -> Unit = sqlStateMonitor::monitor
    val notifications: Notifications = NotificationsNop()
    val requestEvent: (RequestValue) -> Unit = notifications::requestEvent
    val responseEvent: (ResponseValue) -> Unit = notifications::responseEvent
    val eventConnectionLifecycle: Lifecycle<ConnectionWrapper> =
        ConnectionLifecycle(host, user, password, logSqlEvent)
    val eventDatabase = Database(EventSchema, eventConnectionLifecycle)
    val stateConnectionLifecycle: Lifecycle<ConnectionWrapper> =
        ConnectionLifecycle(host, user, password, logSqlState)
    val stateDatabase = Database(StateSchema, stateConnectionLifecycle)
    val eventGenericDatabase: GenericDatabase = GenericDatabaseImpl(
        eventConnectionLifecycle::getValue,
        queryLoader
    )
    val stateGenericDatabase: GenericDatabase = GenericDatabaseImpl(
        stateConnectionLifecycle::getValue,
        queryLoader
    )
    val stateDbQueries: StateDbQueries = StateDbQueriesImpl(stateGenericDatabase)
    val eventDbQueries: EventDbQueries = EventDbQueriesImpl(
        eventGenericDatabase
    )
    val dbEventParser: DbEventParser = DbEventParserImpl()
    val stateDbCommands: StateDbCommands = StateDbCommandsImpl(stateGenericDatabase)
    val eventDbCommands: EventDbCommands = EventDbCommandsImpl(
        eventGenericDatabase,
        eventDbQueries,
        stateDbCommands,
        dbEventParser,
        clock
    )
    val syncDbCommands: StateDbCommands = SyncDbCommands(eventDbCommands)
    val service: Service = ApiService(passwordUtil, syncDbCommands, stateDbQueries)
    val lifecycles: Lifecycles = ServiceLifecycles(
        eventConnectionLifecycle = eventConnectionLifecycle,
        stateConnectionLifecycle = stateConnectionLifecycle
    )
    val eventInitializer: Initializer = SchemaInitializer(lifecycles::eventConnection, EventSchema, queryLoader)
    val stateInitializer: Initializer = SchemaInitializer(lifecycles::stateConnection, StateSchema, queryLoader)
    val initializer: Initializer = CompositeInitializer(eventInitializer, stateInitializer)
    val serviceCommandParser:ServiceCommandParser = ServiceCommandParserImpl()
    private val files: FilesContract = FilesDelegate
    private val charset: Charset = StandardCharsets.UTF_8
    val keyBasePath:Path = Paths.get("src/test/resources")
    val algorithmFactory: AlgorithmFactory = AlgorithmFactoryImpl(files, charset, keyBasePath)
    val cipher:Cipher = CipherImpl(algorithmFactory)
    val handler: Handler =
        ApiHandler(serviceCommandParser, service, cipher, requestEvent, responseEvent)
    val cookieSimulator: CookieSimulator = CookieSimulator()
    val regressionTestRunner: RegressionTestRunner = RegressionTestRunnerImpl(
        snapshotDir,
        snapshotViews,
        lifecycles,
        initializer,
        requestEvents,
        handler,
        eventDatabase,
        stateDatabase,
        sqlEventMonitor,
        sqlStateMonitor,
        cookieSimulator,
        cipher
    )
}