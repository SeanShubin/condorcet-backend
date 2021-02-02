package com.seanshubin.condorcet.backend.genericdb

import java.sql.ResultSet

interface GenericDatabase {
    fun <T> queryExactlyOneRow(
        createFunction: (ResultSet) -> T,
        queryPath: String,
        vararg parameters: Any?
    ): T

    fun <T> queryZeroOrOneRow(
        createFunction: (ResultSet) -> T,
        queryPath: String,
        vararg parameters: Any?
    ): T?

    fun <T> query(
        createFunction: (ResultSet) -> T,
        queryPath: String,
        vararg parameters: Any?
    ): List<T>

    fun queryExactlyOneInt(queryPath: String, vararg parameters: Any?): Int
    fun queryZeroOrOneInt(queryPath: String, vararg parameters: Any?): Int?
    fun update(queryPath: String, vararg parameters: Any?): Int
}