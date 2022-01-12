package com.seanshubin.condorcet.backend.console

import com.seanshubin.condorcet.backend.contract.FilesContract
import com.seanshubin.condorcet.backend.logger.LogGroup
import com.seanshubin.condorcet.backend.logger.Logger
import com.seanshubin.condorcet.backend.logger.LoggerFactory
import com.seanshubin.condorcet.backend.server.LoggingNotifications
import com.seanshubin.condorcet.backend.server.Notifications
import com.seanshubin.condorcet.backend.server.NotificationsFactory
import java.nio.charset.Charset
import java.nio.file.Path
import java.nio.file.Paths

class RegressionDataImpl(
    val loggerFactory: LoggerFactory,
    val logDir: Path,
    val phase: Phase,
    val files: FilesContract,
    val charset: Charset
) : RegressionData, NotificationsFactory {
    override fun loadText(regressionFile: RegressionFile): String {
        val path = fullPath(regressionFile)
        return if (files.exists(path)) files.readString(fullPath(regressionFile), charset)
        else ""
    }

    override fun namePath(regressionFile: RegressionFile): Path = regressionFile.toNamePath()
    override fun fullPath(regressionFile: RegressionFile): Path = logDir.resolve(namePath(regressionFile))
    override fun createNotifications(logDir: Path): Notifications {
        val logGroup: LogGroup = loggerFactory.createLogGroupWithoutTimestamp(logDir)
        val rootDatabaseLogger: Logger = createLogger(logGroup, RegressionFile.ROOT)
        val eventDatabaseLogger: Logger = createLogger(logGroup, RegressionFile.EVENT)
        val stateDatabaseLogger: Logger = createLogger(logGroup, RegressionFile.STATE)
        val eventTableLogger: Logger = createLogger(logGroup, RegressionFile.EVENT_TABLE)
        val stateTableLogger: Logger = createLogger(logGroup, RegressionFile.STATE_TABLE)
        val httpLogger: Logger = createLogger(logGroup, RegressionFile.HTTP)
        val topLevelExceptionLogger: Logger = createLogger(logGroup, RegressionFile.TOP_LEVEL_EXCEPTION)
        return LoggingNotifications(
            rootDatabaseLogger,
            eventDatabaseLogger,
            stateDatabaseLogger,
            eventTableLogger,
            stateTableLogger,
            httpLogger,
            topLevelExceptionLogger
        )
    }

    private fun RegressionFile.toNamePath(): Path =
        Paths.get("regression-$fileName-${phase.name.toLowerCase()}.$extension")

    private fun createLogger(logGroup: LogGroup, regressionFile: RegressionFile): Logger {
        val fullPath = fullPath(regressionFile)
        val namePath = namePath(regressionFile)
        return if (phase == Phase.EXPECTED && files.exists(fullPath)) {
            Logger.nop
        } else {
            files.deleteIfExists(fullPath)
            logGroup.create(namePath)
        }
    }
}
