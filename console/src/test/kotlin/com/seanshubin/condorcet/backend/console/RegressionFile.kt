package com.seanshubin.condorcet.backend.console

import java.nio.charset.Charset
import java.nio.file.Path

enum class RegressionFile(val fileName: String, val extension: String) {
    EVENT("event", "sql"),
    STATE("state", "sql"),
    HTTP("http", "txt");

    fun toRegressionInfoFile(baseDir: Path, charset: Charset, phase: Phase): RegressionInfoFile =
        RegressionInfoFile(baseDir, charset, phase, fileName, extension)
}
