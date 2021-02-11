package com.seanshubin.condorcet.backend.server

import com.seanshubin.condorcet.backend.http.RequestValue
import com.seanshubin.condorcet.backend.http.ResponseValue

interface Notifications {
    fun databaseEvent(databaseCommand: String)
    fun requestEvent(requestValue: RequestValue)
    fun responseEvent(responseValue: ResponseValue)
}
