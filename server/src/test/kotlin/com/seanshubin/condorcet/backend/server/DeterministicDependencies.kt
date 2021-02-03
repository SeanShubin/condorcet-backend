package com.seanshubin.condorcet.backend.server

import com.seanshubin.condorcet.backend.crypto.*
import com.seanshubin.condorcet.backend.genericdb.*
import com.seanshubin.condorcet.backend.io.ClassLoaderUtil
import com.seanshubin.condorcet.backend.service.*
import org.eclipse.jetty.server.Handler
import java.nio.file.Path
import java.time.Clock

class DeterministicDependencies(snapshotDir: Path) {
    val realClock: Clock = Clock.systemUTC()
    val clockPath = snapshotDir.resolve("clock.txt")
    val clock: Clock = RememberingClock(realClock, clockPath)
    val uniqueIdsPath = snapshotDir.resolve("unique-ids.txt")
    val serviceEventParser: ServiceEventParser = ServiceEventParserImpl()
    val realUniqueIdGenerator: UniqueIdGenerator = Uuid4()
    val uniqueIdGenerator: UniqueIdGenerator = RememberingUuidGenerator(realUniqueIdGenerator, uniqueIdsPath)
    val oneWayHash: OneWayHash = Sha256Hash()
    val passwordUtil: PasswordUtil = PasswordUtil(uniqueIdGenerator, oneWayHash)
    private fun loadResource(name: String): String = ClassLoaderUtil.loadResourceAsString("sql/$name")
    val host: String = "localhost"
    val user: String = "root"
    val password: String = "insecure"
    val sqlEvent: (String) -> Unit = ::println
    val eventConnectionLifecycle: Lifecycle<ConnectionWrapper> =
        ConnectionLifecycle(host, user, password, sqlEvent)
    val eventDatabase = Database(EventSchema, eventConnectionLifecycle)
    val stateConnectionLifecycle: Lifecycle<ConnectionWrapper> =
        ConnectionLifecycle(host, user, password, sqlEvent)
    val stateDatabase = Database(StateSchema, stateConnectionLifecycle)
    val eventGenericDatabase: GenericDatabase = GenericDatabaseImpl(
        eventConnectionLifecycle::getValue,
        ::loadResource
    )
    val stateGenericDatabase: GenericDatabase = GenericDatabaseImpl(
        stateConnectionLifecycle::getValue,
        ::loadResource
    )
    val stateDbQueries: StateDbQueries = StateDbQueriesFromResources(stateGenericDatabase)
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
    val lifecycles: Lifecycles = DomainLifecycles(
        eventConnectionLifecycle = eventConnectionLifecycle,
        stateConnectionLifecycle = stateConnectionLifecycle
    )
    val eventInitializer: Initializer = SchemaInitializer(lifecycles::eventConnection, EventSchema)
    val stateInitializer: Initializer = SchemaInitializer(lifecycles::stateConnection, StateSchema)
    val initializer: Initializer = CompositeInitializer(eventInitializer, stateInitializer)
    val handler: Handler = ApiHandler(serviceEventParser, service)
}