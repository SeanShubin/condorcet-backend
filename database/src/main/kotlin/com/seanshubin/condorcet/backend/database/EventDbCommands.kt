package com.seanshubin.condorcet.backend.database

interface EventDbCommands {
    fun addEvent(authority: String, type: String, body: String)
    fun setLastSynced(lastSynced: Int)
    fun initializeLastSynced(lastSynced: Int)
}
