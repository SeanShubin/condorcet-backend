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
    private val phase: Phase
) : Notifications {
    val event = RegressionInfoFile(phase, "event.sql")
    val state = RegressionInfoFile(phase, "state.sql")
    val http = RegressionInfoFile(phase, "http.txt")
    val list = listOf(event, state, http)

    init {
        Files.createDirectories(baseDir)
    }

    override fun eventDatabaseEvent(statement: String) {
        event.println(formatStatement(statement))
    }

    override fun stateDatabaseEvent(statement: String) {
        state.println(formatStatement(statement))
    }

    override fun requestEvent(request: RequestValue) {
        request.toLines().forEach {
            http.println(it)
        }
    }

    override fun responseEvent(response: ResponseValue) {
        response.toLines().forEach {
            http.println(it)
        }
        http.println("")
    }

    fun formatStatement(statement: String): String = statement.trim().replace(whitespace, " ") + ";"

    enum class Phase {
        EXPECTED,
        ACTUAL;
    }

    inner class RegressionInfoFile(phase: Phase, name: String) {
        val path = baseDir.resolve("regression-${phase.name.toLowerCase()}-$name")
        val active: Boolean = when (phase) {
            Phase.EXPECTED -> {
                !Files.exists(path)
            }
            Phase.ACTUAL -> {
                Files.deleteIfExists(path)
                true
            }
        }

        fun println(line: String) {
            if (active) {
                Files.writeString(path, line + "\n", charset, StandardOpenOption.APPEND, StandardOpenOption.CREATE)
            }
        }
    }

    companion object {
        val whitespace = Regex("""\s+""")
    }
}
