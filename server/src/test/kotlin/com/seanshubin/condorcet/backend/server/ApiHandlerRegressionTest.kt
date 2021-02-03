package com.seanshubin.condorcet.backend.server

import com.seanshubin.condorcet.backend.crypto.*
import com.seanshubin.condorcet.backend.genericdb.*
import com.seanshubin.condorcet.backend.io.ClassLoaderUtil
import com.seanshubin.condorcet.backend.json.JsonMappers
import com.seanshubin.condorcet.backend.service.*
import org.eclipse.jetty.server.Handler
import org.eclipse.jetty.server.Request
import org.junit.Test
import java.io.BufferedReader
import java.io.PrintWriter
import java.io.StringReader
import java.io.StringWriter
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import kotlin.test.assertEquals

class ApiHandlerRegressionTest {

    @Test
    fun regressionTest() {
        // given
        val commands = listOf(
            ServiceEvent.AddUser(name = "Alice", email = "alice@email.com", password = "alice-password"),
            ServiceEvent.AddUser(name = "duplicate-email", email = "alice@email.com", password = "alice-password"),
            ServiceEvent.AddUser(name = "Alice", email = "duplicate@name.com", password = "alice-password"),
            ServiceEvent.AddUser(name = "Bob", email = "bob@email.com", password = "bob-password"),
            ServiceEvent.AddUser(name = "Carol", email = "carol@email.com", password = "carol-password"),
            ServiceEvent.AddUser(name = "Dave", email = "dave@email.com", password = "dave-password"),
            ServiceEvent.Authenticate(nameOrEmail = "Alice", password = "alice-password"),
            ServiceEvent.Authenticate(nameOrEmail = "alice@email.com", password = "alice-password"),
            ServiceEvent.Authenticate(nameOrEmail = "Alice", password = "wrong-password"),
            ServiceEvent.Authenticate(nameOrEmail = "alice@email.com", password = "wrong-password"),
            ServiceEvent.Authenticate(nameOrEmail = "Nobody", password = "password")
        )
        val tester = Tester(commands)
        tester.createMissingExpectations()

        // when
        tester.generateActual()

        // then
        tester.validateExpectationsAgainstActual()
    }

    class Database(val schema: Schema, val lifecycle: Lifecycle<ConnectionWrapper>)

    interface Snapshot {
        val name: String
        fun getPath(snapshotDir: Path, annotation: String): Path {
            return snapshotDir.resolve("regression-$name-$annotation.txt")
        }

        fun getLines(info: SnapshotInfo): List<String>
        fun loadLines(snapshotDir: Path, annotation: String): List<String> {
            return Files.readAllLines(getPath(snapshotDir, annotation))
        }
    }

    object ApiSnapshot : Snapshot {
        override val name = "api"

        override fun getLines(info: SnapshotInfo): List<String> {
            return info.events.flatMap { it.toLines() }
        }
    }

    object EventSnapshot : Snapshot {
        override val name = "event"
        override fun getLines(info: SnapshotInfo): List<String> {
            return info.eventTables.flatMap { it.toLines() }
        }
    }

    object StateSnapshot : Snapshot {
        override val name = "state"
        override fun getLines(info: SnapshotInfo): List<String> {
            return info.eventTables.flatMap { it.toLines() }
        }
    }

    data class SnapshotInfo(
        val events: List<RegressionTestEvent>,
        val eventTables: List<GenericTable>,
        val stateTables: List<GenericTable>
    )

    class RequestStub(
        private val name: String,
        private val theMethod: String,
        private val body: String
    ) : HttpServletRequestNotImplemented() {
        private val stringReader = StringReader(body)
        private val theReader = BufferedReader(stringReader)
        override fun getMethod(): String = theMethod
        override fun getReader(): BufferedReader {
            return theReader
        }
    }

    class ResponseStub : HttpServletResponseNotImplemented() {
        var theContentType: String? = null
        var theStatus: Int? = null
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)
        override fun setContentType(type: String?) {
            theContentType = type
        }

        override fun getStatus(): Int {
            return theStatus!!
        }

        override fun setStatus(sc: Int) {
            theStatus = sc
        }

        override fun getWriter(): PrintWriter {
            return printWriter
        }
    }

    class RememberingUuidGenerator(val backing: UniqueIdGenerator, val path: Path) : UniqueIdGenerator {
        var index = 0
        val previous: MutableList<String> = if (Files.exists(path)) Files.readAllLines(path) else mutableListOf()
        fun reset() {
            index = 0
        }

        override fun uniqueId(): String {
            if (index < previous.size) {
                val result = previous[index]
                index++
                return result
            } else {
                val result = backing.uniqueId()
                previous.add(result)
                index++
                Files.write(path, listOf(result), StandardOpenOption.CREATE, StandardOpenOption.APPEND)
                return result
            }
        }
    }

    class Tester(private val serviceEvents: List<ServiceEvent>) {
        private val charset: Charset = StandardCharsets.UTF_8
        private val clock: MinuteAtATime = MinuteAtATime()
        private val snapshotDir = Paths.get("src", "test", "resources")
        private val snapshots = listOf(ApiSnapshot, EventSnapshot, StateSnapshot)
        private val snapshotPathApi = snapshotDir.resolve("regression-api.txt")
        private val snapshotPathEvent = snapshotDir.resolve("regression-event.txt")
        private val snapshotPathState = snapshotDir.resolve("regression-state.txt")
        private val uniqueIdsPath = snapshotDir.resolve("unique-ids.txt")
        private val serviceEventParser: ServiceEventParser = ServiceEventParserImpl()
        private val realUniqueIdGenerator: UniqueIdGenerator = Uuid4()
        private val uniqueIdGenerator: RememberingUuidGenerator =
            RememberingUuidGenerator(realUniqueIdGenerator, uniqueIdsPath)
        private val oneWayHash: OneWayHash = Sha256Hash()
        private val passwordUtil: PasswordUtil = PasswordUtil(uniqueIdGenerator, oneWayHash)
        private fun loadResource(name: String): String = ClassLoaderUtil.loadResourceAsString("sql/$name")
        private val host: String = "localhost"
        private val user: String = "root"
        private val password: String = "insecure"
        private val sqlEvent: (String) -> Unit = ::println
        private val eventConnectionLifecycle: Lifecycle<ConnectionWrapper> =
            ConnectionLifecycle(host, user, password, sqlEvent)
        private val eventDatabase = Database(EventSchema, eventConnectionLifecycle)
        private val stateConnectionLifecycle: Lifecycle<ConnectionWrapper> =
            ConnectionLifecycle(host, user, password, sqlEvent)
        private val stateDatabase = Database(StateSchema, stateConnectionLifecycle)
        private val eventGenericDatabase: GenericDatabase = GenericDatabaseImpl(
            eventConnectionLifecycle::getValue,
            ::loadResource
        )
        private val stateGenericDatabase: GenericDatabase = GenericDatabaseImpl(
            stateConnectionLifecycle::getValue,
            ::loadResource
        )
        private val stateDbQueries: StateDbQueries = StateDbQueriesFromResources(stateGenericDatabase)
        private val eventDbQueries: EventDbQueries = EventDbQueriesImpl(
            eventGenericDatabase
        )
        private val dbEventParser: DbEventParser = DbEventParserImpl()
        private val stateDbCommands: StateDbCommands = StateDbCommandsImpl(stateGenericDatabase)
        private val eventDbCommands: EventDbCommands = EventDbCommandsImpl(
            eventGenericDatabase,
            eventDbQueries,
            stateDbCommands,
            dbEventParser,
            clock
        )
        private val syncDbCommands: StateDbCommands = SyncDbCommands(eventDbCommands)
        private val service: Service = ApiService(passwordUtil, syncDbCommands, stateDbQueries)
        private val lifecycles: Lifecycles = DomainLifecycles(
            eventConnectionLifecycle = eventConnectionLifecycle,
            stateConnectionLifecycle = stateConnectionLifecycle
        )
        private val eventInitializer: Initializer = SchemaInitializer(lifecycles::eventConnection, EventSchema)
        private val stateInitializer: Initializer = SchemaInitializer(lifecycles::stateConnection, StateSchema)
        private val initializer: Initializer = CompositeInitializer(eventInitializer, stateInitializer)
        private val handler: Handler = ApiHandler(serviceEventParser, service)

        fun createMissingExpectations() {
            val missingSnapshots = snapshots.filter { !Files.exists(it.getPath(snapshotDir, "expect")) }
            if (missingSnapshots.isNotEmpty()) {
                val snapshotInfo = generateSnapshotInfo()
                missingSnapshots.forEach {
                    val snapshotLines = it.getLines(snapshotInfo)
                    Files.write(it.getPath(snapshotDir, "expect"), snapshotLines)
                }
            }
        }

        fun generateActual() {
            val snapshotInfo = generateSnapshotInfo()
            snapshots.forEach {
                val snapshotLines = it.getLines(snapshotInfo)
                Files.write(it.getPath(snapshotDir, "actual"), snapshotLines)
            }
        }

        fun validateExpectationsAgainstActual() {
            snapshots.forEach(::validateSnapshot)
        }

        fun validateSnapshot(snapshot: Snapshot) {
            val expectLines = snapshot.loadLines(snapshotDir, "expect")
            val actualLines = snapshot.loadLines(snapshotDir, "actual")
            val pairs = expectLines zip actualLines
            pairs.forEachIndexed { index, (expect, actual) ->
                assertEquals(expect, actual, "failure on line $index in ${snapshot.name}")
            }
            assertEquals(expectLines.size, actualLines.size, "sizes different in ${snapshot.name}")
        }

        fun generateSnapshotInfo(): SnapshotInfo =
            doInLifecycle {
                lifecycles.openAll()
                uniqueIdGenerator.reset()
                clock.reset()
                initializer.reset()
                initializer.initialize()
                val events = serviceEvents.map { runCommand(handler, it) }
                val eventTables = queryTables(eventDatabase)
                val stateTables = queryTables(stateDatabase)
                SnapshotInfo(events, eventTables, stateTables)
            }

        fun queryTables(database: Database): List<GenericTable> {
            val schema = database.schema
            val schemaName = schema.name
            return schema.tables.map { table ->
                val tableName = table.name
                val query = "select * from $tableName"
                database.lifecycle.getValue().update("use $schemaName")
                database.lifecycle.getValue().queryGenericTable(query)
            }
        }

        fun <T> doInLifecycle(f: () -> T): T {
            try {
                lifecycles.openAll()
                return f()
            } finally {
                lifecycles.closeAll()
            }
        }


        fun debug() {
            doInLifecycle {
                val databases = listOf(eventDatabase, stateDatabase)
                databases.forEach { database ->
                    val schema = database.schema
                    val schemaName = schema.name
                    schema.tables.forEach { table ->
                        val tableName = table.name
                        val query = "select * from $tableName"
                        database.lifecycle.getValue().update("use $schemaName")
                        val genericTable = database.lifecycle.getValue().queryGenericTable(query)
                        genericTable.toLines().forEach(::println)
                    }

                }

            }
        }

        private fun runCommand(
            handler: Handler,
            serviceEvent: ServiceEvent
        ): RegressionTestEvent {
            val name = serviceEvent.javaClass.simpleName
            val method = "POST"
            val requestBody = JsonMappers.pretty.writeValueAsString(serviceEvent)
            val target = "/$name"
            val request = RequestStub(name, method, requestBody)
            val baseRequest = Request(null, null)
            val response = ResponseStub()
            handler.handle(target, baseRequest, request, response)
            val statusCode = response.status
            val responseBody = response.stringWriter.buffer.toString()
            val event = RegressionTestEvent(name, method, requestBody, statusCode, responseBody)
            return event
        }
    }

}
