package com.seanshubin.condorcet.backend.server

import com.seanshubin.condorcet.backend.genericdb.GenericTable
import com.seanshubin.condorcet.backend.http.RequestValue
import com.seanshubin.condorcet.backend.http.ResponseValue

interface Notifications {
    fun rootDatabaseEvent(statement: String)
    fun eventDatabaseEvent(statement: String)
    fun stateDatabaseEvent(statement: String)
    fun eventTableEvent(table: GenericTable)
    fun stateTableEvent(table: GenericTable)
    fun requestEvent(request: RequestValue)
    fun responseEvent(response: ResponseValue)
}
