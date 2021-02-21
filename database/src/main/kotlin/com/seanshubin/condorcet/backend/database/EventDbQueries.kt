package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.genericdb.GenericDatabase

interface EventDbQueries : GenericDatabase {
    fun eventsToSync(lastEventSynced: Int): List<EventRow>
    fun eventCount(): Int
}
