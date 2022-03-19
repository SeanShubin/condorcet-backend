package com.seanshubin.condorcet.backend.genericdb

import java.sql.ResultSet

interface GenericDatabase {
    fun <T> queryExactlyOneRow(
        createFunction: (ResultSet) -> T,
        name: String,
        vararg parameters: Any?
    ): T

    fun <T> queryZeroOrOneRow(
        createFunction: (ResultSet) -> T,
        name: String,
        vararg parameters: Any?
    ): T?

    fun queryExists(
        name: String,
        vararg parameters: Any?
    ): Boolean

    fun <T> query(
        createFunction: (ResultSet) -> T,
        name: String,
        vararg parameters: Any?
    ): List<T>

    fun <T> queryStreaming(
        createFunction: (ResultSet) -> T,
        processFunction: (T) -> Unit,
        name:String,
        vararg parameters:Any?)

    fun <ParentType, ChildType, ParentKeyType, ResultType> queryParentChild(
        parentFunction: (ResultSet) -> ParentType,
        childFunction: (ResultSet) -> ChildType,
        parentKeyFunction: (ResultSet) -> ParentKeyType,
        composeFunction: (ParentType, List<ChildType>) -> ResultType,
        name: String,
        vararg parameters: Any?
    ): List<ResultType>

    fun queryUntyped(
        name: String,
        code: String,
        vararg parameters: Any?
    ): GenericTable

    fun queryExactlyOneInt(name: String, vararg parameters: Any?): Int
    fun queryZeroOrOneInt(name: String, vararg parameters: Any?): Int?
    fun update(name: String, vararg parameters: Any?): Int
    fun updateUsingScript(name: String)
    fun updateUsingSqlList(name: String, sqlList: List<String>)
    fun tableNames(schema: Schema): List<String>
    fun tableData(schema: Schema, name: String): GenericTable
    fun debugTableData(schema: Schema, tableName: String): GenericTable
    fun debugQuery(name:String, sql: String)
    fun purgeDatabase(name: String)
    fun createDatabase(name: String)
    fun useDatabase(name: String)
}