package com.seanshubin.condorcet.backend.server

import com.seanshubin.condorcet.backend.logger.LogGroup
import com.seanshubin.condorcet.backend.logger.Logger
import com.seanshubin.condorcet.backend.logger.LoggerFactory
import java.nio.file.Path

class LoggingNotificationsFactory : NotificationsFactory {
    override fun createNotifications(logDir: Path): Notifications {
        val logGroup: LogGroup = LoggerFactory.instanceDefaultZone.createLogGroupWithTimestamp(logDir)
        val rootDatabaseLogger: Logger = logGroup.create("root", "sql")
        val eventDatabaseLogger: Logger = logGroup.create("event", "sql")
        val stateDatabaseLogger: Logger = logGroup.create("state", "sql")
        val eventTableLogger: Logger = logGroup.create("event-snapshot", "sql")
        val stateTableLogger: Logger = logGroup.create("state-snapshot", "sql")
        val httpLogger: Logger = logGroup.create("http")
        val serviceRequestLogger: Logger = logGroup.create("service-request")
        val serviceResponseLogger: Logger = logGroup.create("service-response")
        val topLevelExceptionLogger: Logger = logGroup.create("exception")
        val sqlExceptionLogger: Logger = logGroup.create("sql-exception")
        val sendMailLogger: Logger = logGroup.create("mail")
        return LoggingNotifications(
            rootDatabaseLogger,
            eventDatabaseLogger,
            stateDatabaseLogger,
            eventTableLogger,
            stateTableLogger,
            httpLogger,
            serviceRequestLogger,
            serviceResponseLogger,
            topLevelExceptionLogger,
            sqlExceptionLogger,
            sendMailLogger
        )
    }
}
