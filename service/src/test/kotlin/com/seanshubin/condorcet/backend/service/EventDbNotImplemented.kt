package com.seanshubin.condorcet.backend.service

import com.seanshubin.condorcet.backend.database.EventDbCommands
import com.seanshubin.condorcet.backend.database.EventDbQueries
import com.seanshubin.condorcet.backend.database.EventRow
import com.seanshubin.condorcet.backend.genericdb.GenericTable
import com.seanshubin.condorcet.backend.genericdb.Schema
import java.sql.ResultSet

interface EventDbNotImplemented : EventDbQueries, EventDbCommands {
    override fun addEvent(authority: String, type: String, body: String) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun eventsToSync(lastEventSynced: Int): List<EventRow> {
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
}