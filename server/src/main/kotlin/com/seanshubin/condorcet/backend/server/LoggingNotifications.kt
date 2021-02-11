package com.seanshubin.condorcet.backend.server

import com.seanshubin.condorcet.backend.http.RequestValue
import com.seanshubin.condorcet.backend.http.ResponseValue
import com.seanshubin.condorcet.backend.logger.LogGroup
import com.seanshubin.condorcet.backend.logger.Logger
import com.seanshubin.condorcet.backend.logger.LoggerFactory
import java.nio.file.Path

class LoggingNotifications(logDir: Path) : Notifications {
    private val logGroup: LogGroup = LoggerFactory.instanceDefaultZone.createLogGroup(logDir)
    private val databaseLogger: Logger = logGroup.create("sql")
    private val httpLogger: Logger = logGroup.create("http")
    override fun databaseEvent(databaseCommand: String) {
        databaseLogger.log("${databaseCommand.trim()};")
    }

    override fun requestEvent(requestValue: RequestValue) {
        httpLogger.log("target = '${requestValue.target}'")
        requestValue.headers.list.forEach{ (name, value) ->
            httpLogger.log("$name -> $value")
        }
        httpLogger.log(requestValue.body)
    }

    override fun responseEvent(responseValue: ResponseValue) {
        httpLogger.log("status = ${responseValue.status}")
        responseValue.headers.list.forEach{ (name, value) ->
            httpLogger.log("$name -> $value")
        }
        val body = responseValue.body
        if(body != null){
            httpLogger.log(body)
        }
    }
}
