package com.seanshubin.condorcet.backend.genericdb

import com.mysql.cj.PreparedQuery
import com.mysql.cj.jdbc.ClientPreparedStatement
import java.sql.*
import java.time.Instant

class ConnectionWrapper(
    private val connection: Connection,
    private val sqlEvent: (String) -> Unit,
    private val sqlException: (String, String, SQLException) -> Unit
) : AutoCloseable {
    fun <T> query(name: String, code: String, vararg parameters: Any?, f: (ResultSet) -> T): T {
        val statement = connection.prepareStatement(code) as ClientPreparedStatement
        updateParameters(name, parameters, statement)
        return statement.use {
            f(executeQuery(name, statement))
        }
    }

    fun <T> queryList(name: String, code: String, vararg parameters: Any?, f: (ResultSet) -> T): List<T> {
        val list = mutableListOf<T>()
        queryStreaming(name, code, *parameters) { resultSet ->
            list.add(f(resultSet))
        }
        return list
    }

    fun queryStreaming(name: String, code: String, vararg parameters: Any?, f: (ResultSet) -> Unit) {
        val statement = connection.prepareStatement(code) as ClientPreparedStatement
        updateParameters(name, parameters, statement)
        statement.use {
            val resultSet = executeQuery(name, statement)
            while (resultSet.next()) {
                f(resultSet)
            }
        }
    }

    fun queryExists(name: String, code: String, vararg parameters: Any?): Boolean {
        val statement = connection.prepareStatement(code) as ClientPreparedStatement
        updateParameters(name, parameters, statement)
        statement.use {
            val resultSet = executeQuery(name, statement)
            return resultSet.next()
        }
    }

    fun queryGenericTable(name: String, code: String, vararg parameters: Any?): GenericTable {
        val statement = connection.prepareStatement(code) as ClientPreparedStatement
        updateParameters(name, parameters, statement)
        return statement.use {
            val resultSet = executeQuery(name, statement)
            val iterator = ResultSetIterator.consume(resultSet)
            val columnNames = iterator.columnNames
            val rows = iterator.consumeRemainingToTable()
            GenericTable(name, code, columnNames, rows)
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

    fun update(name: String, code: String, vararg parameters: Any?): Int {
        val statement = connection.prepareStatement(code) as ClientPreparedStatement
        updateParameters(name, parameters, statement)
        return statement.use {
            executeUpdate(name, statement)
        }
    }

    fun tableNames(schema: Schema): List<String> =
        schema.tables.map { it.name }

    fun tableData(schema: Schema, name: String): GenericTable {
        val table = schema.tables.find { it.name == name }!!
        return queryGenericTable(name, table.toSelectAllStatement())
    }

    fun debugQuery(
        name: String,
        sql: String,
        vararg parameters: Any?
    ) {
        val statement = connection.prepareStatement(sql) as ClientPreparedStatement
        updateParameters("debugQuery()", parameters, statement)
        val table = statement.use {
            val resultSet = statement.executeQuery()
            val iterator = ResultSetIterator.consume(resultSet)
            val columnNames = iterator.columnNames
            val rows = iterator.consumeRemainingToTable()
            GenericTable(name, statement.asSql(), columnNames, rows)
        }
        table.toLines().forEach(::println)
    }

    override fun close() {
        connection.close()
    }

    private fun updateParameters(name: String, parameters: Array<out Any?>, statement: ClientPreparedStatement) {
        try {
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
        } catch (ex: SQLException) {
            throw SQLException("$name\n${statement.asSql()}\n${ex.message}", ex)
        }
    }

    private fun executeUpdate(name: String, statement: PreparedStatement): Int {
        try {
            sqlEvent(statement.asSql())
            return statement.executeUpdate()
        } catch (ex: SQLException) {
            sqlException(name, statement.asSql(), ex)
            throw SQLException("$name\n${statement.asSql()}\n${ex.message}", ex)
        }
    }

    private fun executeQuery(name: String, statement: PreparedStatement): ResultSet {
        try {
            sqlEvent(statement.asSql())
            return statement.executeQuery()
        } catch (ex: SQLException) {
            sqlException(name, statement.asSql(), ex)
            throw SQLException("$name\n${statement.asSql()}\n${ex.message}", ex)
        }
    }

    private fun Statement.asSql(): String {
        this as ClientPreparedStatement
        val query = this.query as PreparedQuery
        return query.asSql()
    }
}
