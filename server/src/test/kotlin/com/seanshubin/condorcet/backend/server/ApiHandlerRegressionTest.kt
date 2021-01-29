package com.seanshubin.condorcet.backend.server

import com.seanshubin.condorcet.backend.crypto.*
import com.seanshubin.condorcet.backend.domain.*
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
import kotlin.test.assertEquals

class ApiHandlerRegressionTest {
    @Test
    fun regressionTest() {
        // given
        val commands = listOf(
            Command.AddUser(name = "Alice", email = "alice@email.com", password = "alice-password"),
            Command.AddUser(name = "duplicate-email", email = "alice@email.com", password = "alice-password"),
            Command.AddUser(name = "Alice", email = "duplicate@name.com", password = "alice-password"),
            Command.AddUser(name = "Bob", email = "bob@email.com", password = "bob-password"),
            Command.AddUser(name = "Carol", email = "carol@email.com", password = "carol-password"),
            Command.AddUser(name = "Dave", email = "dave@email.com", password = "dave-password"),
            Command.Authenticate(nameOrEmail = "Alice", password = "alice-password"),
            Command.Authenticate(nameOrEmail = "alice@email.com", password = "alice-password"),
            Command.Authenticate(nameOrEmail = "Alice", password = "wrong-password"),
            Command.Authenticate(nameOrEmail = "alice@email.com", password = "wrong-password"),
            Command.Authenticate(nameOrEmail = "Nobody", password = "password")
        )
        val tester = Tester(commands)
        tester.createSnapshotIfMissing()
        val expected = tester.loadSnapshot()

        // when
        val actual = tester.runCommands()

        // then
        assertEquals(expected, actual)
    }

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

    class Tester(private val commands: List<Command>) {
        private val snapshotDir = Paths.get("src", "test", "resources")
        private val snapshotName = "regression-snapshot.txt"
        private val snapshotPath = snapshotDir.resolve(snapshotName)
        private val charset: Charset = StandardCharsets.UTF_8
        private val parser: Parser = ApiParser()
        private val uniqueIdGenerator: UniqueIdGenerator = Uuid4()
        private val oneWayHash: OneWayHash = Sha256Hash()
        private val passwordUtil: PasswordUtil = PasswordUtil(uniqueIdGenerator, oneWayHash)

        fun createSnapshotIfMissing() {
            if (!Files.exists(snapshotPath)) {
                createSnapshot()
            }
        }

        fun loadSnapshot(): List<Event> {
            val eventLines: List<String> = Files.readAllLines(snapshotPath)
            val initialEvents = listOf<Event>()
            return eventLinesToEvents(eventLines, initialEvents)
        }

        tailrec
        fun eventLinesToEvents(eventLines: List<String>, events: List<Event>): List<Event> {
            if (eventLines.isEmpty()) return events
            else {
                val (remainingEventLines, event) = Event.consumeFromLines(eventLines)
                val eventsSoFar = events + event
                return eventLinesToEvents(remainingEventLines, eventsSoFar)
            }

        }

        fun runCommands(): List<Event> {
            val service: Service = ApiService(passwordUtil)
            val handler: Handler = ApiHandler(parser, service)
            return commands.map { runCommand(handler, it) }
        }

        private fun createSnapshot() {
            Files.createDirectories(snapshotDir)
            val events = runCommands()
            storeEvents(events)
        }

        private fun runCommand(handler: Handler, command: Command): Event {
            val name = command.javaClass.simpleName
            val method = "POST"
            val requestBody = JsonMappers.pretty.writeValueAsString(command)
            val target = "/$name"
            val request = RequestStub(name, method, requestBody)
            val baseRequest = Request(null, null)
            val response = ResponseStub()
            handler.handle(target, baseRequest, request, response)
            val statusCode = response.status
            val responseBody = response.stringWriter.buffer.toString()
            val event = Event(name, method, requestBody, statusCode, responseBody)
            return event
        }

        private fun storeEvents(events: List<Event>) {
            val eventLines = events.flatMap { it.toLines() }
            Files.write(snapshotPath, eventLines, charset)
        }
    }

}
