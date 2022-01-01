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

    fun <ParentType, ChildType, KeyType, ResultType> queryJoin(
        parentFunction: (ResultSet) -> ParentType,
        childFunction: (ResultSet) -> ChildType,
        keyFunction: (ResultSet) -> KeyType,
        mergeFunction: (ParentType, List<ChildType>) -> ResultType,
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
    fun tableNames(schema: Schema): List<String>
    fun tableData(schema: Schema, name: String): GenericTable
    fun debugTableData(schema: Schema, name: String): GenericTable
}