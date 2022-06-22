package com.seanshubin.condorcet.backend.service

import com.seanshubin.condorcet.backend.database.Event
import com.seanshubin.condorcet.backend.database.ImmutableDbCommands
import com.seanshubin.condorcet.backend.database.ImmutableDbQueries
import com.seanshubin.condorcet.backend.genericdb.GenericTable
import com.seanshubin.condorcet.backend.genericdb.Schema
import java.io.PrintWriter
import java.sql.ResultSet
import java.time.Instant

interface ImmutableDbNotImplemented : ImmutableDbQueries, ImmutableDbCommands {
    override fun addEvent(authority: String, type: String, body: String, now: Instant) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun eventsToSync(lastEventSynced: Int): List<Event> {
        throw UnsupportedOperationException("not implemented")
    }

    override fun eventCount(): Int {
        throw UnsupportedOperationException("not implemented")
    }

    override fun <T> queryExactlyOneRow(createFunction: (ResultSet) -> T, name: String, vararg parameters: Any?): T {
        throw UnsupportedOperationException("not implemented")
    }

    override fun <T> queryZeroOrOneRow(createFunction: (ResultSet) -> T, name: String, vararg parameters: Any?): T? {
        throw UnsupportedOperationException("not implemented")
    }

    override fun queryExists(name: String, vararg parameters: Any?): Boolean {
        throw UnsupportedOperationException("not implemented")
    }

    override fun <T> query(createFunction: (ResultSet) -> T, name: String, vararg parameters: Any?): List<T> {
        throw UnsupportedOperationException("not implemented")
    }

    override fun queryUntyped(name: String, code: String, vararg parameters: Any?): GenericTable {
        throw UnsupportedOperationException("not implemented")
    }

    override fun queryExactlyOneInt(name: String, vararg parameters: Any?): Int {
        throw UnsupportedOperationException("not implemented")
    }

    override fun queryZeroOrOneInt(name: String, vararg parameters: Any?): Int? {
        throw UnsupportedOperationException("not implemented")
    }

    override fun update(name: String, vararg parameters: Any?): Int {
        throw UnsupportedOperationException("not implemented")
    }

    override fun tableNames(schema: Schema): List<String> {
        throw UnsupportedOperationException("not implemented")
    }

    override fun tableData(schema: Schema, name: String): GenericTable {
        throw UnsupportedOperationException("not implemented")
    }

    override fun debugTableData(schema: Schema, name: String): GenericTable {
        throw UnsupportedOperationException("not implemented")
    }

    override fun <ParentType, ChildType, KeyType, ResultType> queryParentChild(
        parentFunction: (ResultSet) -> ParentType,
        childFunction: (ResultSet) -> ChildType,
        keyFunction: (ResultSet) -> KeyType,
        mergeFunction: (ParentType, List<ChildType>) -> ResultType,
        name: String,
        vararg parameters: Any?
    ): List<ResultType> {
        throw UnsupportedOperationException("not implemented")
    }

    override fun debugQuery(name: String, sql: String) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateUsingScript(name: String) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun purgeDatabase(name: String) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun createDatabase(name: String) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun useDatabase(name: String) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateUsingSqlList(name: String, sqlList: List<String>) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun backupToWriter(writer: PrintWriter) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun <T> queryStreaming(
        createFunction: (ResultSet) -> T,
        processFunction: (T) -> Unit,
        name: String,
        vararg parameters: Any?
    ) {
        throw UnsupportedOperationException("not implemented")
    }
}