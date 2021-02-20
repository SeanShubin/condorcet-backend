package com.seanshubin.condorcet.backend.genericdb

import com.seanshubin.condorcet.backend.genericdb.ConnectionWrapper.Companion.createInt
import java.sql.ResultSet

class GenericDatabaseImpl(
    private val connection: ConnectionWrapper,
    private val queryLoader: QueryLoader
) : GenericDatabase {
    override fun <T> queryExactlyOneRow(
        createFunction: (ResultSet) -> T,
        name: String,
        vararg parameters: Any?
    ): T {
        val code = queryLoader.load(name)
        return connection.queryExactlyOneRow(name, code, *parameters) { createFunction(it) }
    }

    override fun <T> queryZeroOrOneRow(
        createFunction: (ResultSet) -> T,
        name: String,
        vararg parameters: Any?
    ): T? {
        val code = queryLoader.load(name)
        return connection.queryZeroOrOneRow(name, code, *parameters) { createFunction(it) }
    }

    override fun queryExists(name: String, vararg parameters: Any?): Boolean {
        val code = queryLoader.load(name)
        return connection.queryExists(name, code, *parameters)
    }

    override fun <T> query(
        createFunction: (ResultSet) -> T,
        name: String,
        vararg parameters: Any?
    ): List<T> {
        val code = queryLoader.load(name)
        return connection.queryList(name, code, *parameters) { createFunction(it) }
    }

    override fun queryUntyped(query: String, vararg parameters: Any?): GenericTable =
        connection.queryGenericTable(query)

    override fun queryExactlyOneInt(queryPath: String, vararg parameters: Any?): Int =
        queryExactlyOneRow(::createInt, queryPath, *parameters)

    override fun queryZeroOrOneInt(queryPath: String, vararg parameters: Any?): Int? =
        queryZeroOrOneRow(::createInt, queryPath, *parameters)

    override fun update(name: String, vararg parameters: Any?): Int {
        val code = queryLoader.load(name)
        return connection.update(name, code, *parameters)
    }

    override fun tableNames(schema: Schema): List<String> =
        schema.tables.map { it.name }

    override fun tableData(schema: Schema, name: String): GenericTable {
        val table = schema.tables.find { it.name == name }!!
        return connection.queryGenericTable(table.toSelectAllStatement())
    }
}
