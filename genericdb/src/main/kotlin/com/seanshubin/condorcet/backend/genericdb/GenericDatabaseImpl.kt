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

    override fun queryUntyped(name: String, code: String, vararg parameters: Any?): GenericTable =
        connection.queryGenericTable(name, code)

    override fun queryExactlyOneInt(name: String, vararg parameters: Any?): Int =
        queryExactlyOneRow(::createInt, name, *parameters)

    override fun queryZeroOrOneInt(name: String, vararg parameters: Any?): Int? =
        queryZeroOrOneRow(::createInt, name, *parameters)

    override fun update(name: String, vararg parameters: Any?): Int {
        val code = queryLoader.load(name)
        return connection.update(name, code, *parameters)
    }

    override fun tableNames(schema: Schema): List<String> =
        schema.tables.map { it.name }

    override fun tableData(schema: Schema, name: String): GenericTable {
        val table = schema.tables.find { it.name == name }!!
        return connection.queryGenericTable(name, table.toSelectAllStatement())
    }

    override fun debugTableData(schema: Schema, name: String): GenericTable {
        val code = queryLoader.load("debug-$name")
        return connection.queryGenericTable(name, code)
    }

    override fun <ParentType, ChildType, KeyType, ResultType> queryJoin(
        parentFunction: (ResultSet) -> ParentType,
        childFunction: (ResultSet) -> ChildType,
        keyFunction: (ResultSet) -> KeyType,
        mergeFunction: (ParentType, List<ChildType>) -> ResultType,
        name: String,
        vararg parameters: Any?
    ): List<ResultType> {
        throw UnsupportedOperationException("not implemented")
    }
}
