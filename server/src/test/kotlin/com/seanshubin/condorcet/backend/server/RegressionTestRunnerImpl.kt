package com.seanshubin.condorcet.backend.server

import com.seanshubin.condorcet.backend.genericdb.GenericTable
import com.seanshubin.condorcet.backend.genericdb.Initializer
import com.seanshubin.condorcet.backend.json.JsonMappers
import com.seanshubin.condorcet.backend.service.Lifecycles
import jakarta.servlet.http.HttpServletResponse
import org.eclipse.jetty.server.Handler
import org.eclipse.jetty.server.Request
import java.nio.file.Files
import java.nio.file.Path

class RegressionTestRunnerImpl(
    val snapshotDir: Path,
    val snapshotViews: List<SnapshotView>,
    val lifecycles: Lifecycles,
    val initializer: Initializer,
    val requestEvents: List<RequestEvent>,
    val handler: Handler,
    val eventDatabase: Database,
    val stateDatabase: Database,
    val sqlEventMonitor: SqlMonitor,
    val sqlStateMonitor: SqlMonitor,
    val cookieSimulator: CookieSimulator
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
            val events = requestEvents.map(::runCommand)
            val sqlEventStatements = sqlEventMonitor.getSqlStatements()
            val sqlStateStatements = sqlStateMonitor.getSqlStatements()
            val eventTables = queryTables(eventDatabase)
            val stateTables = queryTables(stateDatabase)
            Snapshot(events, eventTables, stateTables, sqlEventStatements, sqlStateStatements)
        }

    private fun runCommand(requestEvent: RequestEvent): RegressionTestEvent {
        val serviceRequest = requestEvent.serviceRequest
        val requestHeaders = requestEvent.headers
        val name = serviceRequest.javaClass.simpleName
        val method = "POST"
        val requestBody = JsonMappers.pretty.writeValueAsString(serviceRequest)
        val target = "/$name"
        val request = RequestStub(name, method, requestBody, cookieSimulator, requestHeaders)
        val requestCookies = cookieSimulator.cookieList.toList()
        val baseRequest = Request(null, null)
        val response = ResponseStub(cookieSimulator)
        val responseHeaders = getHeaders(response)
        cookieSimulator.addCookieInvocations.clear()
        handler.handle(target, baseRequest, request, response)
        val responseCookies = cookieSimulator.addCookieInvocations.toList()
        val statusCode = response.status
        val responseBody = response.stringWriter.buffer.toString()
        val event = RegressionTestEvent(
            name,
            method,
            requestBody,
            requestHeaders,
            requestCookies,
            statusCode,
            responseBody,
            responseHeaders,
            responseCookies
        )
        return event
    }

    private fun getHeaders(request: HttpServletResponse): List<Pair<String, String>> =
        request.headerNames.toList().map {
            Pair(it, request.getHeader(it))
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