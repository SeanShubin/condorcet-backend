package com.seanshubin.condorcet.backend.console

import java.nio.charset.Charset
import java.nio.file.Path

enum class RegressionFile(val fileName: String, val extension: String) {
    EVENT("event", "sql"),
    EVENT_TABLE("event-table", "txt"),
    STATE("state", "sql"),
    STATE_TABLE("state-table", "txt"),
    HTTP("http", "txt");

    fun toRegressionInfoFile(baseDir: Path, charset: Charset, phase: Phase): RegressionInfoFile =
        RegressionInfoFile(baseDir, charset, phase, fileName, extension)
}
