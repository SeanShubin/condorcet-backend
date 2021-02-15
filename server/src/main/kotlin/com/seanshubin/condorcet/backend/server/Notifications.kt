package com.seanshubin.condorcet.backend.server

import com.seanshubin.condorcet.backend.http.RequestValue
import com.seanshubin.condorcet.backend.http.ResponseValue

interface Notifications {
    fun eventDatabaseEvent(statement: String)
    fun stateDatabaseEvent(statement: String)
    fun requestEvent(request: RequestValue)
    fun responseEvent(response: ResponseValue)
}
