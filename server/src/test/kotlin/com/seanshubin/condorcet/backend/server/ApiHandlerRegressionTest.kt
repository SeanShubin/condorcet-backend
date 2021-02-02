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
import java.nio.file.Paths
import java.time.Clock
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
        tester.createSnapshotIfMissing()
        val expected = tester.loadSnapshot()

        // when
        val actual = tester.runCommands()

        // then
        assertEquals(expected, actual)
    }

    data class Snapshot(val events: List<RegressionTestEvent>)

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

    class Tester(private val serviceEvents: List<ServiceEvent>) {
        private val clock: Clock = Clock.systemUTC()
        private val snapshotDir = Paths.get("src", "test", "resources")
        private val snapshotName = "regression-snapshot.txt"
        private val snapshotPath = snapshotDir.resolve(snapshotName)
        private val charset: Charset = StandardCharsets.UTF_8
        private val serviceEventParser: ServiceEventParser = ServiceEventParserImpl()
        private val uniqueIdGenerator: UniqueIdGenerator = Uuid4()
        private val oneWayHash: OneWayHash = Sha256Hash()
        private val passwordUtil: PasswordUtil = PasswordUtil(uniqueIdGenerator, oneWayHash)
        private fun loadResource(name: String): String = ClassLoaderUtil.loadResourceAsString("sql/$name")
        private val host: String = "localhost"
        private val user: String = "root"
        private val password: String = "insecure"
        private val sqlEvent: (String) -> Unit = ::println
        private val eventConnectionLifecycle: Lifecycle<ConnectionWrapper> =
            ConnectionLifecycle(host, user, password, sqlEvent)
        private val stateConnectionLifecycle: Lifecycle<ConnectionWrapper> =
            ConnectionLifecycle(host, user, password, sqlEvent)
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

        fun createSnapshotIfMissing() {
            if (!Files.exists(snapshotPath)) {
                createSnapshot()
            }
        }

        fun loadSnapshot(): Snapshot {
            val eventLines: List<String> = Files.readAllLines(snapshotPath)
            val initialEvents = listOf<RegressionTestEvent>()
            val events = eventLinesToEvents(eventLines, initialEvents)
            return Snapshot(events)
        }

        tailrec fun eventLinesToEvents(
            eventLines: List<String>,
            regressionTestEvents: List<RegressionTestEvent>
        ): List<RegressionTestEvent> {
            if (eventLines.isEmpty()) return regressionTestEvents
            else {
                val (remainingEventLines, event) = RegressionTestEvent.consumeFromLines(eventLines)
                val eventsSoFar = regressionTestEvents + event
                return eventLinesToEvents(remainingEventLines, eventsSoFar)
            }

        }

        fun runCommands(): Snapshot {
            try {
                lifecycles.openAll()
                initializer.reset()
                initializer.initialize()
                val events = serviceEvents.map { runCommand(handler, it) }
                return Snapshot(events)
            } finally {
                lifecycles.closeAll()
            }
        }

        private fun createSnapshot() {
            Files.createDirectories(snapshotDir)
            val snapshot = runCommands()
            storeSnapshot(snapshot)
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

        private fun storeSnapshot(snapshot: Snapshot) {
            val eventLines = snapshot.events.flatMap { it.toLines() }
            Files.write(snapshotPath, eventLines, charset)
        }
    }

}
