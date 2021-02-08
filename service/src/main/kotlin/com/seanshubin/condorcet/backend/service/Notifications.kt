package com.seanshubin.condorcet.backend.service

interface Notifications {
    fun databaseEvent(databaseCommand: String)
    fun requestEvent(target: String, body: String)
    fun responseEvent(status: Int, body: String)
}
