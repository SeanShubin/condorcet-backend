package com.seanshubin.condorcet.backend.server

import com.seanshubin.condorcet.backend.genericdb.GenericTable
import com.seanshubin.condorcet.backend.json.JsonMappers
import com.seanshubin.condorcet.backend.service.ServiceEvent
import org.eclipse.jetty.server.Handler
import org.eclipse.jetty.server.Request
import org.junit.Test
import java.io.BufferedReader
import java.io.PrintWriter
import java.io.StringReader
import java.io.StringWriter
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
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
        val snapshotDir = Paths.get("src", "test", "resources")
        val tester = Tester(commands)
        tester.createMissingExpectations(snapshotDir, DeterministicDependencies(snapshotDir))

        // when
        tester.generateActual(snapshotDir, DeterministicDependencies(snapshotDir))

        // then
        tester.validateExpectationsAgainstActual(snapshotDir)
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


    class Tester(
        private val serviceEvents: List<ServiceEvent>
    ) {
        val snapshots = listOf(
            SnapshotView.Api,
            SnapshotView.Event,
            SnapshotView.State
        )

        fun createMissingExpectations(snapshotDir: Path, dependencies: DeterministicDependencies) {
            val missingSnapshots = snapshots.filter { !Files.exists(it.getPath(snapshotDir, "expect")) }
            if (missingSnapshots.isNotEmpty()) {
                val snapshotInfo = generateSnapshotInfo(dependencies)
                missingSnapshots.forEach {
                    val snapshotLines = it.getLines(snapshotInfo)
                    Files.write(it.getPath(snapshotDir, "expect"), snapshotLines)
                }
            }
        }

        fun generateActual(snapshotDir: Path, dependencies: DeterministicDependencies) {
            val snapshotInfo = generateSnapshotInfo(dependencies)
            snapshots.forEach {
                val snapshotLines = it.getLines(snapshotInfo)
                Files.write(it.getPath(snapshotDir, "actual"), snapshotLines)
            }
        }

        fun validateExpectationsAgainstActual(snapshotDir: Path) {
            snapshots.forEach { validateSnapshot(snapshotDir, it) }
        }

        fun validateSnapshot(snapshotDir: Path, snapshotView: SnapshotView) {
            val expectLines = snapshotView.loadLines(snapshotDir, "expect")
            val actualLines = snapshotView.loadLines(snapshotDir, "actual")
            val pairs = expectLines zip actualLines
            pairs.forEachIndexed { index, (expect, actual) ->
                assertEquals(expect, actual, "failure on line $index in ${snapshotView.name}")
            }
            assertEquals(expectLines.size, actualLines.size, "sizes different in ${snapshotView.name}")
        }

        fun generateSnapshotInfo(dependencies: DeterministicDependencies): Snapshot =
            doInLifecycle(dependencies) {
                dependencies.lifecycles.openAll()
                dependencies.initializer.reset()
                dependencies.initializer.initialize()
                val events = serviceEvents.map { runCommand(dependencies.handler, it) }
                val eventTables = queryTables(dependencies.eventDatabase)
                val stateTables = queryTables(dependencies.stateDatabase)
                Snapshot(events, eventTables, stateTables)
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

        fun <T> doInLifecycle(dependencies: DeterministicDependencies, f: () -> T): T {
            try {
                dependencies.lifecycles.openAll()
                return f()
            } finally {
                dependencies.lifecycles.closeAll()
            }
        }


        fun debug(dependencies: DeterministicDependencies) {
            doInLifecycle(dependencies) {
                val databases = listOf(dependencies.eventDatabase, dependencies.stateDatabase)
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
