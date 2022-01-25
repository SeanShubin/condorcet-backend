package com.seanshubin.condorcet.backend.database

interface EventCommands {
    fun addEvent(authority: String, type: String, body: String)
}
