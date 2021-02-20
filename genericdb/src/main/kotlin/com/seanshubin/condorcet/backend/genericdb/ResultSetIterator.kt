package com.seanshubin.condorcet.backend.genericdb

import java.sql.ResultSet

class ResultSetIterator(
    private val resultSet: ResultSet,
    val columnNames: List<String>
) : Iterator<List<Any>> {
    private var resultSetNext = resultSet.next()
    override fun hasNext(): Boolean = resultSetNext

    override fun next(): List<Any> {
        val rowCells = columnNames.map { resultSet.getObject(it) }
        resultSetNext = resultSet.next()
        return rowCells
    }

    fun consumeRemainingToTable(): List<List<Any>> {
        val list = mutableListOf<List<Any>>()
        forEachRemaining { list.add(it) }
        return list
    }

    companion object {
        fun consume(resultSet: ResultSet): ResultSetIterator {
            val metaData = resultSet.metaData
            val columnNames = (1..metaData.columnCount).map { metaData.getColumnLabel(it) }
            return ResultSetIterator(resultSet, columnNames)
        }
    }
}
