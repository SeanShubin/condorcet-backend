package com.seanshubin.condorcet.backend.server

import com.seanshubin.condorcet.backend.service.Notifications

class NotificationsNop : Notifications {
    override fun databaseEvent(databaseCommand: String) {
    }

    override fun requestEvent(target: String, body: String) {
    }

    override fun responseEvent(status: Int, body: String) {
    }
}