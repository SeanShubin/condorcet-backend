package com.seanshubin.condorcet.backend.genericdb

import com.seanshubin.condorcet.backend.table.RowStyleTableFormatter
import com.seanshubin.condorcet.backend.table.TableFormatter.Companion.plainString

data class GenericTable(
    val queryString: String,
    val columnNames: List<String>,
    val rows: List<List<Any>>
) {
    fun toLines(): List<String> {
        val tableData = listOf(columnNames) + rows
        val tableLines = tableFormatter.format(tableData)
        return listOf(queryString) + tableLines
    }

    companion object {
        val tableFormatter = RowStyleTableFormatter.boxDrawing.copy(cellToString = plainString)
    }
}
