package com.seanshubin.condorcet.backend.console

import com.seanshubin.condorcet.backend.crypto.*
import com.seanshubin.condorcet.backend.database.*
import com.seanshubin.condorcet.backend.genericdb.*
import com.seanshubin.condorcet.backend.logger.LogGroup
import com.seanshubin.condorcet.backend.logger.Logger
import com.seanshubin.condorcet.backend.logger.LoggerFactory
import com.seanshubin.condorcet.backend.server.ApiHandler
import com.seanshubin.condorcet.backend.server.JettyServer
import com.seanshubin.condorcet.backend.server.ServerContract
import com.seanshubin.condorcet.backend.server.ServerRunner
import com.seanshubin.condorcet.backend.service.*
import org.eclipse.jetty.server.Handler
import org.eclipse.jetty.server.Server
import java.nio.file.Path
import java.nio.file.Paths
import java.time.Clock

class Dependencies {
    private val host: String = "localhost"
    private val user: String = "root"
    private val password: String = "insecure"
    private val logDir: Path = Paths.get("out", "log")
    private val logGroup: LogGroup = LoggerFactory.instanceDefaultZone.createLogGroup(logDir)
    private val sqlLogger: Logger = logGroup.create("sql")
    private val sqlEvent: (String) -> Unit = LogDecorators.logSql(sqlLogger)
    private val eventConnectionLifecycle: Lifecycle<ConnectionWrapper> =
        ConnectionLifecycle(host, user, password, sqlEvent)
    private val stateConnectionLifecycle: Lifecycle<ConnectionWrapper> =
        ConnectionLifecycle(host, user, password, sqlEvent)
    private val lifecycles: Lifecycles = ServiceLifecycles(
        eventConnectionLifecycle = eventConnectionLifecycle,
        stateConnectionLifecycle = stateConnectionLifecycle
    )
    private val queryLoader: QueryLoader = QueryLoaderFromResource()
    private val eventInitializer: Initializer = SchemaInitializer(lifecycles::eventConnection, EventSchema, queryLoader)
    private val stateInitializer: Initializer = SchemaInitializer(lifecycles::stateConnection, StateSchema, queryLoader)
    private val initializer: Initializer = CompositeInitializer(eventInitializer, stateInitializer)
    private val port: Int = 8080
    private val server: Server = Server(port)
    private val serverContract: ServerContract = JettyServer(server)
    private val serviceEventParser: ServiceEventParser = ServiceEventParserImpl()
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
    private val eventDbCommands: EventDbCommands = EventDbCommandsImpl(
        eventGenericDatabase,
        eventDbQueries,
        stateDbCommands,
        dbEventParser,
        clock
    )
    private val syncDbCommands: StateDbCommands = SyncDbCommands(eventDbCommands)
    private val stateDbQueries: StateDbQueries = StateDbQueriesImpl(stateGenericDatabase)
    private val service: Service = ApiService(passwordUtil, syncDbCommands, stateDbQueries)
    private val handler: Handler = ApiHandler(serviceEventParser, service)
    val runner: Runnable = ServerRunner(lifecycles, initializer, serverContract, handler)
}
