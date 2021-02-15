package com.seanshubin.condorcet.backend.console

import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

class RegressionInfoFile(
    baseDir: Path,
    val charset: Charset,
    val phase: Phase,
    name: String,
    extension: String
) {
    val path = baseDir.resolve("regression-$name-${phase.name.toLowerCase()}.$extension")
    var active: Boolean = false
    fun initialize() {
        active = when (phase) {
            Phase.EXPECTED -> {
                !Files.exists(path)
            }
            Phase.ACTUAL -> {
                Files.deleteIfExists(path)
                true
            }
        }
    }

    fun println(line: String) {
        if (active) {
            Files.writeString(path, line + "\n", charset, StandardOpenOption.APPEND, StandardOpenOption.CREATE)
        }
    }

    fun load(): String = Files.readString(path, charset)
}
