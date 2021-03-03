package com.seanshubin.condorcet.backend.server

import com.seanshubin.condorcet.backend.genericdb.GenericTable
import com.seanshubin.condorcet.backend.http.RequestValue
import com.seanshubin.condorcet.backend.http.ResponseValue
import com.seanshubin.condorcet.backend.logger.Logger

class LoggingNotifications(
    private val rootDatabaseLogger: Logger,
    private val eventDatabaseLogger: Logger,
    private val stateDatabaseLogger: Logger,
    private val eventTableLogger: Logger,
    private val stateTableLogger: Logger,
    private val httpLogger: Logger
) : Notifications {
    override fun rootDatabaseEvent(statement: String) {
        rootDatabaseLogger.log("${statement.trim()};")
    }

    override fun eventDatabaseEvent(statement: String) {
        eventDatabaseLogger.log("${statement.trim()};")
    }

    override fun stateDatabaseEvent(statement: String) {
        stateDatabaseLogger.log("${statement.trim()};")
    }

    override fun eventTableEvent(table: GenericTable) {
        table.toLines().forEach(eventTableLogger::log)
    }

    override fun stateTableEvent(table: GenericTable) {
        table.toLines().forEach(stateTableLogger::log)
    }

    override fun requestEvent(request: RequestValue) {
        request.toLines().forEach(httpLogger::log)
    }

    override fun responseEvent(response: ResponseValue) {
        response.toLines().forEach(httpLogger::log)
        httpLogger.log("")
    }
}
