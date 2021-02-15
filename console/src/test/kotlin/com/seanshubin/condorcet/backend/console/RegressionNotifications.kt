package com.seanshubin.condorcet.backend.console

import com.seanshubin.condorcet.backend.http.RequestValue
import com.seanshubin.condorcet.backend.http.ResponseValue
import com.seanshubin.condorcet.backend.server.Notifications
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

class RegressionNotifications(
    private val baseDir: Path,
    private val charset: Charset,
    private val phase: String
) : Notifications {
    val eventDatabasePath = baseDir.resolve("regression-$phase-event.sql")
    val stateDatabasePath = baseDir.resolve("regression-$phase-state.sql")
    val httpPath = baseDir.resolve("regression-$phase-http.txt")

    init {
        Files.createDirectories(baseDir)
        Files.deleteIfExists(eventDatabasePath)
        Files.deleteIfExists(stateDatabasePath)
        Files.deleteIfExists(httpPath)
    }

    override fun eventDatabaseEvent(statement: String) {
        val conciseStatement = statement.replace(whitespace, " ")
        emitLine(eventDatabasePath, "$conciseStatement;")
    }

    override fun stateDatabaseEvent(statement: String) {
        val conciseStatement = statement.replace(whitespace, " ")
        emitLine(stateDatabasePath, "$conciseStatement;")
    }

    override fun requestEvent(request: RequestValue) {
        request.toLines().forEach {
            emitLine(httpPath, it)
        }
    }

    override fun responseEvent(response: ResponseValue) {
        response.toLines().forEach {
            emitLine(httpPath, it)
        }
        emitLine(httpPath, "")
    }

    fun emitLine(path: Path, line: String) {
        Files.writeString(path, line + "\n", charset, StandardOpenOption.APPEND, StandardOpenOption.CREATE)
    }

    companion object {
        val whitespace = Regex("""\s+""")
    }
}
