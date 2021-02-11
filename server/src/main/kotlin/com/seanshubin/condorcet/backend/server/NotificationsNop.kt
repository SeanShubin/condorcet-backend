package com.seanshubin.condorcet.backend.server

import com.seanshubin.condorcet.backend.http.RequestValue
import com.seanshubin.condorcet.backend.http.ResponseValue

class NotificationsNop : Notifications {
    override fun databaseEvent(databaseCommand: String) {
    }

    override fun requestEvent(requestValue: RequestValue) {
    }

    override fun responseEvent(responseValue: ResponseValue) {
    }
}