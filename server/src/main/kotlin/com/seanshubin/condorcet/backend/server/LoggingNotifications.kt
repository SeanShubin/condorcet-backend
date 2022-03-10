package com.seanshubin.condorcet.backend.server

import com.seanshubin.condorcet.backend.domain.ElectionUpdates
import com.seanshubin.condorcet.backend.domain.Ranking
import com.seanshubin.condorcet.backend.domain.Role
import com.seanshubin.condorcet.backend.genericdb.GenericTable
import com.seanshubin.condorcet.backend.http.RequestValue
import com.seanshubin.condorcet.backend.http.ResponseValue
import com.seanshubin.condorcet.backend.logger.Logger
import com.seanshubin.condorcet.backend.service.AccessToken
import com.seanshubin.condorcet.backend.service.Tokens
import java.io.PrintWriter
import java.io.StringWriter
import java.sql.SQLException

class LoggingNotifications(
    private val rootDatabaseLogger: Logger,
    private val eventDatabaseLogger: Logger,
    private val stateDatabaseLogger: Logger,
    private val eventTableLogger: Logger,
    private val stateTableLogger: Logger,
    private val httpLogger: Logger,
    private val serviceRequestLogger:Logger,
    private val serviceResponseLogger:Logger,
    private val topLevelExceptionLogger:Logger,
    private val sqlExceptionLogger:Logger
) : Notifications {
    private val serviceRequestsToMonitor = listOf(
        "register",
        "setRole",
        "removeUser",
        "addElection",
        "launchElection",
        "finalizeElection",
        "updateElection",
        "deleteElection",
        "setCandidates",
        "castBallot",
        "setEligibleVoters",
        "changePassword"
        )


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

    override fun httpRequestEvent(request: RequestValue) {
        request.toLines().forEach(httpLogger::log)
    }

    override fun httpResponseEvent(response: ResponseValue) {
        response.toLines().forEach(httpLogger::log)
        httpLogger.log("")
    }

    override fun serviceRequestEvent(name:String, request: String) {
        if(serviceRequestsToMonitor.contains(name)){
            serviceRequestLogger.log("$name($request)")
        }
    }

    override fun serviceResponseEvent(name:String, request: String, response: String) {
        serviceResponseLogger.log("$name($request)")
        serviceResponseLogger.log(response)
        serviceResponseLogger.log("")
    }

    override fun topLevelException(throwable: Throwable) {
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)
        throwable.printStackTrace(printWriter)
        topLevelExceptionLogger.log(stringWriter.buffer.toString())
    }

    override fun sqlException(name: String, sqlCode:String, ex: SQLException) {
        sqlExceptionLogger.log(name)
        sqlExceptionLogger.log(sqlCode)
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)
        ex.printStackTrace(printWriter)
        sqlExceptionLogger.log(stringWriter.buffer.toString())
    }
}
