package com.seanshubin.condorcet.backend.genericdb

import com.mysql.cj.jdbc.ClientPreparedStatement
import java.sql.*
import java.time.Instant

class ConnectionWrapper(
    private val connection: Connection,
    private val sqlEvent: (String) -> Unit
) : AutoCloseable {
    fun <T> query(sql: String, vararg parameters: Any?, f: (ResultSet) -> T): T {
        val statement = connection.prepareStatement(sql) as ClientPreparedStatement
        updateParameters(parameters, statement)
        return statement.use {
            sqlEvent(statement.asSql())
            f(executeQuery(sql, statement))
        }
    }

    fun <T> queryList(sql: String, vararg parameters: Any?, f: (ResultSet) -> T): List<T> {
        val list = mutableListOf<T>()
        val statement = connection.prepareStatement(sql) as ClientPreparedStatement
        updateParameters(parameters, statement)
        statement.use {
            val resultSet = executeQuery(sql, statement)
            while (resultSet.next()) {
                list.add(f(resultSet))
            }
        }
        return list
    }

    fun queryGenericTable(sql: String, vararg parameters: Any?): GenericTable {
        val statement = connection.prepareStatement(sql) as ClientPreparedStatement
        sqlEvent(statement.asSql())
        updateParameters(parameters, statement)
        return statement.use {
            val resultSet = executeQuery(sql, statement)
            val iterator = ResultSetIterator.consume(resultSet)
            val columnNames = iterator.columnNames
            val rows = iterator.consumeRemainingToTable()
            GenericTable(sql, columnNames, rows)
        }
    }

    fun <T> queryExactlyOneRow(sql: String, vararg parameters: Any?, f: (ResultSet) -> T): T =
        query(sql, *parameters) { resultSet ->
            if (resultSet.next()) {
                val result = f(resultSet)
                if (resultSet.next()) {
                    throw RuntimeException("No more than 1 row expected for '$sql'")
                }
                result
            } else {
                throw RuntimeException("Exactly 1 row expected for '$sql', got none")
            }
        }

    fun <T> queryZeroOrOneRow(sql: String, vararg parameters: Any?, f: (ResultSet) -> T): T? =
        query(sql, *parameters) { resultSet ->
            if (resultSet.next()) {
                val result = f(resultSet)
                if (resultSet.next()) {
                    throw RuntimeException("No more than 1 row expected for '$sql'")
                }
                result
            } else {
                null
            }
        }

    fun queryExactlyOneInt(sql: String, vararg parameters: Any?): Int =
        queryExactlyOneRow(sql, *parameters) { createInt(it) }

    fun queryZeroOrOneInt(sql: String, vararg parameters: Any?): Int? =
        queryZeroOrOneRow(sql, *parameters) { createInt(it) }

    fun update(sql: String, vararg parameters: Any?): Int {
        val statement = connection.prepareStatement(sql) as ClientPreparedStatement
        updateParameters(parameters, statement)
        return statement.use {
            sqlEvent(statement.asSql())
            executeUpdate(sql, statement)
        }
    }

    override fun close() {
        connection.close()
    }

    private fun updateParameters(parameters: Array<out Any?>, statement: ClientPreparedStatement) {
        parameters.toList().forEachIndexed { index, any ->
            val position = index + 1
            if (any == null) {
                statement.setObject(position, null)
            } else when (any) {
                is String -> statement.setString(position, any)
                is Boolean -> statement.setBoolean(position, any)
                is Int -> statement.setInt(position, any)
                is Instant -> statement.setTimestamp(position, Timestamp.from(any))
                else -> throw UnsupportedOperationException("Unsupported type ${any.javaClass.simpleName}")
            }
        }
    }

    private fun executeUpdate(sql: String, statement: PreparedStatement): Int {
        try {
            return statement.executeUpdate()
        } catch (ex: SQLException) {
            throw SQLException("$sql\n${ex.message}", ex)
        }
    }

    private fun executeQuery(sql: String, statement: PreparedStatement): ResultSet {
        try {
            return statement.executeQuery()
        } catch (ex: SQLException) {
            throw SQLException("$sql\n${ex.message}", ex)
        }
    }

    companion object {
        fun createInt(resultSet: ResultSet): Int = resultSet.getInt(1)
    }
}
