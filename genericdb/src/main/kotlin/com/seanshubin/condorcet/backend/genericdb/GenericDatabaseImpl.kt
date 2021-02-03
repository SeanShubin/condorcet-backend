package com.seanshubin.condorcet.backend.genericdb

import com.seanshubin.condorcet.backend.genericdb.ConnectionWrapper.Companion.createInt
import java.sql.ResultSet

class GenericDatabaseImpl(
    private val getConnection: () -> ConnectionWrapper,
    private val queryLoader: QueryLoader
) : GenericDatabase {
    override fun <T> queryExactlyOneRow(
        createFunction: (ResultSet) -> T,
        sqlResource: String,
        vararg parameters: Any?
    ): T {
        val sql = queryLoader.load(sqlResource)
        return getConnection().queryExactlyOneRow(sql, *parameters) { createFunction(it) }
    }

    override fun <T> queryZeroOrOneRow(
        createFunction: (ResultSet) -> T,
        sqlResource: String,
        vararg parameters: Any?
    ): T? {
        val sql = queryLoader.load(sqlResource)
        return getConnection().queryZeroOrOneRow(sql, *parameters) { createFunction(it) }
    }

    override fun <T> query(
        createFunction: (ResultSet) -> T,
        sqlResource: String,
        vararg parameters: Any?
    ): List<T> {
        val sql = queryLoader.load(sqlResource)
        return getConnection().queryList(sql, *parameters) { createFunction(it) }
    }

    override fun queryExactlyOneInt(sqlResource: String, vararg parameters: Any?): Int =
        queryExactlyOneRow(::createInt, sqlResource, *parameters)

    override fun queryZeroOrOneInt(sqlResource: String, vararg parameters: Any?): Int? =
        queryZeroOrOneRow(::createInt, sqlResource, *parameters)

    override fun update(sqlResource: String, vararg parameters: Any?): Int {
        val sql = queryLoader.load(sqlResource)
        return getConnection().update(sql, *parameters)
    }
}
