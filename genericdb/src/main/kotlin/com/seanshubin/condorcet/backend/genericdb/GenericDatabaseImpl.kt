package com.seanshubin.condorcet.backend.genericdb

import com.seanshubin.condorcet.backend.genericdb.ConnectionWrapper.Companion.createInt
import java.sql.ResultSet

class GenericDatabaseImpl(
    private val connection: ConnectionWrapper,
    private val queryLoader: QueryLoader
) : GenericDatabase {
    override fun <T> queryExactlyOneRow(
        createFunction: (ResultSet) -> T,
        queryPath: String,
        vararg parameters: Any?
    ): T {
        val query = queryLoader.load(queryPath)
        return connection.queryExactlyOneRow(query, *parameters) { createFunction(it) }
    }

    override fun <T> queryZeroOrOneRow(
        createFunction: (ResultSet) -> T,
        queryPath: String,
        vararg parameters: Any?
    ): T? {
        val query = queryLoader.load(queryPath)
        return connection.queryZeroOrOneRow(query, *parameters) { createFunction(it) }
    }

    override fun queryExists(queryPath: String, vararg parameters: Any?): Boolean {
        val query = queryLoader.load(queryPath)
        return connection.queryExists(query, *parameters)
    }

    override fun <T> query(
        createFunction: (ResultSet) -> T,
        queryPath: String,
        vararg parameters: Any?
    ): List<T> {
        val query = queryLoader.load(queryPath)
        return connection.queryList(query, *parameters) { createFunction(it) }
    }

    override fun queryUntyped(query: String, vararg parameters: Any?): GenericTable =
        connection.queryGenericTable(query)

    override fun queryExactlyOneInt(queryPath: String, vararg parameters: Any?): Int =
        queryExactlyOneRow(::createInt, queryPath, *parameters)

    override fun queryZeroOrOneInt(queryPath: String, vararg parameters: Any?): Int? =
        queryZeroOrOneRow(::createInt, queryPath, *parameters)

    override fun update(queryPath: String, vararg parameters: Any?): Int {
        val query = queryLoader.load(queryPath)
        return connection.update(query, *parameters)
    }

    override fun tableNames(schema: Schema): List<String> =
        schema.tables.map { it.name }

    override fun tableData(schema: Schema, name: String): GenericTable {
        val table = schema.tables.find { it.name == name }!!
        return connection.queryGenericTable(table.toSelectAllStatement())
    }
}
