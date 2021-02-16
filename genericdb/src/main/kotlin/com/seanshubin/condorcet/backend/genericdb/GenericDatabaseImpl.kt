package com.seanshubin.condorcet.backend.genericdb

import com.seanshubin.condorcet.backend.genericdb.ConnectionWrapper.Companion.createInt
import java.sql.ResultSet

class GenericDatabaseImpl(
    private val getConnection: () -> ConnectionWrapper,
    private val queryLoader: QueryLoader
) : GenericDatabase {
    override fun <T> queryExactlyOneRow(
        createFunction: (ResultSet) -> T,
        queryPath: String,
        vararg parameters: Any?
    ): T {
        val query = queryLoader.load(queryPath)
        return getConnection().queryExactlyOneRow(query, *parameters) { createFunction(it) }
    }

    override fun <T> queryZeroOrOneRow(
        createFunction: (ResultSet) -> T,
        queryPath: String,
        vararg parameters: Any?
    ): T? {
        val query = queryLoader.load(queryPath)
        return getConnection().queryZeroOrOneRow(query, *parameters) { createFunction(it) }
    }

    override fun queryExists(queryPath: String, vararg parameters: Any?): Boolean {
        val query = queryLoader.load(queryPath)
        return getConnection().queryExists(query, *parameters)
    }

    override fun <T> query(
        createFunction: (ResultSet) -> T,
        queryPath: String,
        vararg parameters: Any?
    ): List<T> {
        val query = queryLoader.load(queryPath)
        return getConnection().queryList(query, *parameters) { createFunction(it) }
    }

    override fun queryUntyped(query: String, vararg parameters: Any?): GenericTable =
        getConnection().queryGenericTable(query)

    override fun queryExactlyOneInt(queryPath: String, vararg parameters: Any?): Int =
        queryExactlyOneRow(::createInt, queryPath, *parameters)

    override fun queryZeroOrOneInt(queryPath: String, vararg parameters: Any?): Int? =
        queryZeroOrOneRow(::createInt, queryPath, *parameters)

    override fun update(queryPath: String, vararg parameters: Any?): Int {
        val query = queryLoader.load(queryPath)
        return getConnection().update(query, *parameters)
    }
}
