package com.seanshubin.condorcet.backend.console

import com.seanshubin.condorcet.backend.dependencies.Dependencies
import com.seanshubin.condorcet.backend.domain.Role
import com.seanshubin.condorcet.backend.domain.TableData
import com.seanshubin.condorcet.backend.service.AccessToken
import com.seanshubin.condorcet.backend.string.util.RowStyleTableFormatter
import java.io.PrintWriter
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

object DocumentationDataApp {
    @JvmStatic
    fun main(args: Array<String>) {
        val dependencies = Dependencies(arrayOf(), ConsoleIntegration())
        val initializer = dependencies.schemaCreator
        val service = dependencies.service
        initializer.purgeAllData()
        initializer.initialize()
        val alice = AccessToken("Alice", Role.OWNER)
        val bob = AccessToken("Bob", Role.OWNER)
        service.register("Alice", "alice@email.com", "pass")
        service.register("Bob", "bob@email.com", "pass")
        service.setRole(alice, "Bob", Role.USER)
        service.addElection(bob, "Bob", "Favorite Color")
        service.setCandidates(bob, "Favorite Color", listOf("Red", "Green", "Blue"))
        val accessToken = AccessToken("Alice", Role.OWNER)
        val path = Paths.get("docs","markdown-tables.md")
        val charset = StandardCharsets.UTF_8
        val bufferedWriter = Files.newBufferedWriter(path, charset, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
        val writer = PrintWriter(bufferedWriter)
        writer.use {
            service.eventData(accessToken).toMarkdownLines().forEach(writer::println)
            service.listTables(alice).forEach{tableName->
                service.tableData(alice, tableName).toMarkdownLines().forEach(writer::println)
            }
        }
    }

    private fun TableData.toMarkdownLines():List<String> {
        val tableFormat = RowStyleTableFormatter.markdown
        val caption = listOf("", "$name table", "")
        val firstRow = columnNames
        val secondRow = columnNames.map{ "---"}
        val headerRows = listOf(firstRow, secondRow)
        val tableRows = headerRows + rows
        val lines = caption + tableFormat.format(tableRows)
        return lines
    }
}
