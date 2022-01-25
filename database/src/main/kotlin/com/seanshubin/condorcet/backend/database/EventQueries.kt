package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.genericdb.GenericDatabase

interface EventQueries : GenericDatabase {
    fun eventsToSync(lastEventSynced: Int): List<EventRow>
    fun eventCount(): Int
}
