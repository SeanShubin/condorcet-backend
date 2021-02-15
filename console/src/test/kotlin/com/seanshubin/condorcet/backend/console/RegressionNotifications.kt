package com.seanshubin.condorcet.backend.console

import com.seanshubin.condorcet.backend.json.JsonMappers
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

class RegressionNotifications(
    private val baseDir: Path,
    private val charset: Charset
) {
    init {
        Files.createDirectories(baseDir)
    }

    fun <T> regressionEvent(phase: String, type: String): (T) -> Unit {
        fun <T> recordEvent(event: T) {
            val asString = JsonMappers.pretty.writeValueAsString(event)
            val path = baseDir.resolve("regression-$phase-$type.txt")
            Files.writeString(path, asString + "\n", charset, StandardOpenOption.APPEND, StandardOpenOption.CREATE)
        }
        return ::recordEvent
    }
}