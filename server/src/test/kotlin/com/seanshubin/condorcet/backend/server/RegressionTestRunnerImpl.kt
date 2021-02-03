package com.seanshubin.condorcet.backend.server

import com.seanshubin.condorcet.backend.genericdb.GenericTable
import com.seanshubin.condorcet.backend.genericdb.Initializer
import com.seanshubin.condorcet.backend.json.JsonMappers
import com.seanshubin.condorcet.backend.service.Lifecycles
import com.seanshubin.condorcet.backend.service.ServiceEvent
import org.eclipse.jetty.server.Handler
import org.eclipse.jetty.server.Request
import java.nio.file.Files
import java.nio.file.Path

class RegressionTestRunnerImpl(
    val snapshotDir: Path,
    val snapshotViews: List<SnapshotView>,
    val lifecycles: Lifecycles,
    val initializer: Initializer,
    val serviceEvents: List<ServiceEvent>,
    val handler: Handler,
    val eventDatabase: Database,
    val stateDatabase: Database,
    val sqlEventMonitor: SqlMonitor,
    val sqlStateMonitor: SqlMonitor
) : RegressionTestRunner {
    override fun createMissingSnapshotsForExpected() {
        val missingSnapshots = snapshotViews.filter { !Files.exists(it.getPath(snapshotDir, "expect")) }
        if (missingSnapshots.isNotEmpty()) {
            val snapshot = generateSnapshot()
            missingSnapshots.forEach {
                val snapshotLines = it.getLines(snapshot)
                Files.write(it.getPath(snapshotDir, "expect"), snapshotLines)
            }
        }
    }

    override fun createSnapshotsForActual() {
        val snapshotInfo = generateSnapshot()
        snapshotViews.forEach {
            val snapshotLines = it.getLines(snapshotInfo)
            Files.write(it.getPath(snapshotDir, "actual"), snapshotLines)
        }
    }

    private fun generateSnapshot() =
        lifecycles.doInLifecycle {
            initializer.purgeAllData()
            initializer.initialize()
            val events = serviceEvents.map(::runCommand)
            val sqlEventStatements = sqlEventMonitor.getSqlStatements()
            val sqlStateStatements = sqlStateMonitor.getSqlStatements()
            val eventTables = queryTables(eventDatabase)
            val stateTables = queryTables(stateDatabase)
            Snapshot(events, eventTables, stateTables, sqlEventStatements, sqlStateStatements)
        }

    private fun runCommand(serviceEvent: ServiceEvent): RegressionTestEvent {
        val name = serviceEvent.javaClass.simpleName
        val method = "POST"
        val requestBody = JsonMappers.pretty.writeValueAsString(serviceEvent)
        val target = "/$name"
        val request = ApiHandlerRegressionTest.RequestStub(name, method, requestBody)
        val baseRequest = Request(null, null)
        val response = ApiHandlerRegressionTest.ResponseStub()
        handler.handle(target, baseRequest, request, response)
        val statusCode = response.status
        val responseBody = response.stringWriter.buffer.toString()
        val event = RegressionTestEvent(name, method, requestBody, statusCode, responseBody)
        return event
    }

    private fun queryTables(database: Database): List<GenericTable> {
        val schema = database.schema
        return schema.tables.map { table ->
            val tableName = table.name
            val query = "select * from $tableName"
            database.lifecycle.getValue().queryGenericTable(query)
        }
    }

}