package com.seanshubin.condorcet.backend.server

import com.seanshubin.condorcet.backend.http.RequestValue
import com.seanshubin.condorcet.backend.http.ResponseValue

class NotificationsNop : Notifications {
    override fun eventDatabaseEvent(statement: String) {
    }

    override fun stateDatabaseEvent(statement: String) {
    }

    override fun requestEvent(request: RequestValue) {
    }

    override fun responseEvent(response: ResponseValue) {
    }
}