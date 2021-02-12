package com.seanshubin.condorcet.backend.database

interface EventDbCommands {
    fun addEvent(authority: String, type: String, body: String)
}
