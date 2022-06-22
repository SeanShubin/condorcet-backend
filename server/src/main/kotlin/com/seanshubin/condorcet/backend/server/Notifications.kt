package com.seanshubin.condorcet.backend.server

import com.seanshubin.condorcet.backend.genericdb.GenericTable
import com.seanshubin.condorcet.backend.http.RequestValue
import com.seanshubin.condorcet.backend.http.ResponseValue
import com.seanshubin.condorcet.backend.mail.SendMailCommand
import java.sql.SQLException

interface Notifications {
    fun rootDatabaseEvent(statement: String)
    fun eventDatabaseEvent(statement: String)
    fun stateDatabaseEvent(statement: String)
    fun eventTableEvent(table: GenericTable)
    fun stateTableEvent(table: GenericTable)
    fun httpRequestEvent(request: RequestValue)
    fun httpResponseEvent(response: ResponseValue)
    fun serviceRequestEvent(name: String, request: String)
    fun serviceResponseEvent(name: String, request: String, response: String)
    fun topLevelException(throwable: Throwable)
    fun sqlException(name: String, sqlCode: String, ex: SQLException)
    fun sendMailEvent(sendMailCommand: SendMailCommand)
}
