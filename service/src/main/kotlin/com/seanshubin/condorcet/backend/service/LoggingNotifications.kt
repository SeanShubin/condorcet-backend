package com.seanshubin.condorcet.backend.service

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

    override fun requestEvent(target: String, body: String) {
        httpLogger.log("target = '$target'")
        httpLogger.log(body)
    }

    override fun responseEvent(status: Int, body: String) {
        httpLogger.log("status = $status")
        httpLogger.log(body)
    }
}