package com.seanshubin.condorcet.backend.server

import com.seanshubin.condorcet.backend.http.RequestValue
import com.seanshubin.condorcet.backend.http.ResponseValue
import com.seanshubin.condorcet.backend.logger.LogGroup
import com.seanshubin.condorcet.backend.logger.Logger
import com.seanshubin.condorcet.backend.logger.LoggerFactory
import java.nio.file.Path

class LoggingNotifications(logDir: Path) : Notifications {
    private val logGroup: LogGroup = LoggerFactory.instanceDefaultZone.createLogGroup(logDir)
    private val eventDatabaseLogger: Logger = logGroup.create("event", "sql")
    private val stateDatabaseLogger: Logger = logGroup.create("state", "sql")
    private val httpLogger: Logger = logGroup.create("http")
    override fun eventDatabaseEvent(statement: String) {
        eventDatabaseLogger.log("${statement.trim()};")
    }

    override fun stateDatabaseEvent(statement: String) {
        stateDatabaseLogger.log("${statement.trim()};")
    }

    override fun requestEvent(request: RequestValue) {
        request.toLines().forEach(httpLogger::log)
    }

    override fun responseEvent(response: ResponseValue) {
        response.toLines().forEach(httpLogger::log)
    }
}
