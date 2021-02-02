package com.seanshubin.condorcet.backend.genericdb

interface EventDbQueries {
    fun eventsToSync(lastEventSynced: Int): List<EventRow>
    fun lastSynced(): Int?
}
