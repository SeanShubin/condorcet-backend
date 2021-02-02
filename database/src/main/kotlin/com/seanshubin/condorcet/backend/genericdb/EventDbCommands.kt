package com.seanshubin.condorcet.backend.genericdb

interface EventDbCommands {
    fun addEvent(type: String, body: String)
    fun setLastSynced(lastSynced: Int)
    fun initializeLastSynced(lastSynced: Int)
}
