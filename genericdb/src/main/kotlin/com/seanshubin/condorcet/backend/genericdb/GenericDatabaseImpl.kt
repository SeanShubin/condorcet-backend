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

    override fun updateUsingScript(name: String) {
        val scriptCode = queryLoader.load(name)
        val statements = scriptCode.split(';').map(String::trim).filter(String::isNotBlank)
        updateUsingSqlList(name, statements)
    }

    override fun updateUsingSqlList(name: String, sqlList: List<String>) {
        sqlList.forEachIndexed { index, code ->
            connection.update("$name[$index]", code)
        }
    }

    override fun tableNames(schema: Schema): List<String> =
        schema.tables.map { it.name }

    override fun tableData(schema: Schema, name: String): GenericTable {
        val table = schema.tables.find { it.name == name }!!
        return connection.queryGenericTable(name, table.toSelectAllStatement())
    }

    override fun debugTableData(schema: Schema, tableName: String): GenericTable {
        val name = "debug-$tableName"
        val code = queryLoader.load(name)
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
        val code = queryLoader.load(name)

        data class Row(val parent: ParentType, val child: ChildType, val key: KeyType)

        val allRows = connection.queryList(name, code, *parameters) {
            Row(parentFunction(it), childFunction(it), keyFunction(it))
        }
        val grouped = allRows.groupBy { it.key }
        val results = grouped.map { (key, rows) ->
            val parent = rows[0].parent
            val children = rows.map { it.child }
            mergeFunction(parent, children)
        }
        return results
    }

    override fun debugQuery(sql: String) {
        connection.debugQuery(sql)
    }

    override fun purgeDatabase(name: String) {
        val purgeMarker = "can_be_purged"
        if (name.contains(purgeMarker)) {
            val sql = "drop database if exists $name"
            connection.update("purgeDatabase('$name')", sql)
        } else {
            throw RuntimeException("Can only purge databases with '$purgeMarker' in their name")
        }
    }

    override fun createDatabase(name: String) {
        val sql = "create database $name"
        connection.update("createDatabase('$name')", sql)
    }

    override fun useDatabase(name: String) {
        val sql = "use $name"
        connection.update("useDatabase('$name')", sql)
    }
}
