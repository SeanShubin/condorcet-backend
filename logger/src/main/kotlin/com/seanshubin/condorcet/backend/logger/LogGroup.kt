package com.seanshubin.condorcet.backend.logger

import com.seanshubin.condorcet.backend.contract.FilesContract
import java.nio.file.Path
import java.nio.file.Paths

class LogGroup(
    private val emit: (String) -> Unit,
    private val files: FilesContract,
    private val baseDir: Path
) {
    fun create(name: String, ext: String = "log"): Logger {
        val relativePath = Paths.get("$name.$ext")
        return create(relativePath)
    }

    fun create(relativePath: Path): Logger {
        val initialize: () -> Unit = {
            files.createDirectories(baseDir)
        }
        return LineEmittingAndFileLogger(initialize, emit, files, baseDir.resolve(relativePath))
    }
}
