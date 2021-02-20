package com.seanshubin.condorcet.backend.genericdb

import com.mysql.cj.jdbc.ClientPreparedStatement
import java.sql.*
import java.time.Instant

class ConnectionWrapper(
    private val connection: Connection,
    private val sqlEvent: (String) -> Unit
) : AutoCloseable {
    fun <T> query(name: String, code: String, vararg parameters: Any?, f: (ResultSet) -> T): T {
        val statement = connection.prepareStatement(code) as ClientPreparedStatement
        updateParameters(parameters, statement)
        return statement.use {
            sqlEvent(statement.asSql())
            f(executeQuery(code, statement))
        }
    }

    fun <T> queryList(name: String, code: String, vararg parameters: Any?, f: (ResultSet) -> T): List<T> {
        val list = mutableListOf<T>()
        val statement = connection.prepareStatement(code) as ClientPreparedStatement
        updateParameters(parameters, statement)
        statement.use {
            val resultSet = executeQuery(code, statement)
            while (resultSet.next()) {
                list.add(f(resultSet))
            }
        }
        return list
    }

    fun queryExists(name: String, code: String, vararg parameters: Any?): Boolean {
        val statement = connection.prepareStatement(code) as ClientPreparedStatement
        updateParameters(parameters, statement)
        statement.use {
            val resultSet = executeQuery(code, statement)
            return resultSet.next()
        }
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

    fun <T> queryExactlyOneRow(name: String, code: String, vararg parameters: Any?, f: (ResultSet) -> T): T =
        query(name, code, *parameters) { resultSet ->
            if (resultSet.next()) {
                val result = f(resultSet)
                if (resultSet.next()) {
                    throw RuntimeException("No more than 1 row expected for '$name'\n$code")
                }
                result
            } else {
                throw RuntimeException("Exactly 1 row expected for '$name', got none\n$code")
            }
        }

    fun <T> queryZeroOrOneRow(name: String, code: String, vararg parameters: Any?, f: (ResultSet) -> T): T? =
        query(name, code, *parameters) { resultSet ->
            if (resultSet.next()) {
                val result = f(resultSet)
                if (resultSet.next()) {
                    throw RuntimeException("No more than 1 row expected for '$name'\n$code")
                }
                result
            } else {
                null
            }
        }

    fun queryExactlyOneInt(name: String, code: String, vararg parameters: Any?): Int =
        queryExactlyOneRow(name, code, *parameters) { createInt(it) }

    fun queryZeroOrOneInt(name: String, code: String, vararg parameters: Any?): Int? =
        queryZeroOrOneRow(name, code, *parameters) { createInt(it) }

    fun update(name: String, code: String, vararg parameters: Any?): Int {
        val statement = connection.prepareStatement(code) as ClientPreparedStatement
        updateParameters(parameters, statement)
        return statement.use {
            sqlEvent(statement.asSql())
            executeUpdate(code, statement)
        }
    }

    fun tableNames(schema: Schema): List<String> =
        schema.tables.map { it.name }

    fun tableData(schema: Schema, name: String): GenericTable {
        val table = schema.tables.find { it.name == name }!!
        return queryGenericTable(table.toSelectAllStatement())
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
