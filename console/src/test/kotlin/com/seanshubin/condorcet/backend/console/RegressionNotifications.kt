package com.seanshubin.condorcet.backend.console

import com.seanshubin.condorcet.backend.http.RequestValue
import com.seanshubin.condorcet.backend.http.ResponseValue
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

class RegressionNotifications(
    private val baseDir: Path,
    private val charset: Charset,
    private val phase: String
) {
    init {
        Files.createDirectories(baseDir)
    }

    fun databaseEvent(statement: String) {
        val conciseStatement = statement.replace(whitespace, " ")
        emitLine("regression-$phase-database.txt", "$conciseStatement;")
    }

    fun requestEvent(request: RequestValue) {
        request.toLines().forEach {
            emitLine("regression-$phase-http.txt", it)
        }
    }

    fun responseEvent(response: ResponseValue) {
        response.toLines().forEach {
            emitLine("regression-$phase-http.txt", it)
        }
        emitLine("regression-$phase-http.txt", "")
    }

    fun emitLine(fileName: String, line: String) {
        val path = baseDir.resolve(fileName)
        Files.writeString(path, line + "\n", charset, StandardOpenOption.APPEND, StandardOpenOption.CREATE)
    }

    companion object {
        val whitespace = Regex("""\s+""")
    }
}
