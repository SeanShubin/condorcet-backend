package com.seanshubin.condorcet.backend.server

import com.fasterxml.jackson.module.kotlin.readValue
import com.seanshubin.condorcet.backend.genericdb.GenericTable
import com.seanshubin.condorcet.backend.genericdb.Initializer
import com.seanshubin.condorcet.backend.json.JsonMappers
import com.seanshubin.condorcet.backend.jwt.Cipher
import com.seanshubin.condorcet.backend.service.AccessToken
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
    val cookieSimulator: CookieSimulator,
    val cipher: Cipher
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
        val accessToken = requestEvent.accessToken
        val requestHeaders = maybeAccessHeader(accessToken) + cookieSimulator.maybeCookieHeader()
        val name = serviceRequest.javaClass.simpleName
        val requestBody = nullIfEmptyJson(JsonMappers.pretty.writeValueAsString(serviceRequest))
        val target = "/$name"
        val method = "POST"
        val request = RequestStub(requestBody, requestHeaders)
        val baseRequest = Request(null, null)
        val response = ResponseStub()
        handler.handle(target, baseRequest, request, response)
        val responseHeaders = getHeaders(response)
        cookieSimulator.trackCookies(response.headers)
        val statusCode = response.status
        val responseBody = nullIfBlank(response.stringWriter.buffer.toString())
        val event = RegressionTestEvent(
            name,
            method,
            requestBody,
            requestHeaders,
            statusCode,
            responseBody,
            responseHeaders
        )
        return event
    }

    private fun nullIfBlank(s: String): String? =
        if (s.isBlank()) null
        else s

    private fun nullIfEmptyJson(s: String): String? =
        if (JsonMappers.parser.readValue<Map<*, *>>(s).isEmpty()) null
        else s

    private fun maybeAccessHeader(accessToken: AccessToken?) = if (accessToken == null) {
        emptyList()
    } else {
        val bearerToken = cipher.encode(
            mapOf(
                "userName" to accessToken.userName,
                "role" to accessToken.role.name
            )
        )
        val authValue = "Bearer $bearerToken"
        val header = Pair("Authorization", authValue)
        listOf(header)
    }

    private fun getHeaders(response: HttpServletResponse): List<Pair<String, String>> =
        response.headerNames.toList().map {
            Pair(it, response.getHeader(it))
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