package com.seanshubin.condorcet.backend.database

interface EventDbQueries {
    fun eventsToSync(lastEventSynced: Int): List<EventRow>
    fun lastSynced(): Int?
}
