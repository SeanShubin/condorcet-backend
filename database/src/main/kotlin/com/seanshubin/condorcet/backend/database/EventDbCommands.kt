package com.seanshubin.condorcet.backend.database

interface EventDbCommands {
    fun addEvent(type: String, body: String)
    fun setLastSynced(lastSynced: Int)
    fun initializeLastSynced(lastSynced: Int)
}
