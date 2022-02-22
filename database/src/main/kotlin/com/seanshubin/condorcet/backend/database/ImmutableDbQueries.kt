package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.genericdb.GenericDatabase
import java.io.PrintWriter
import java.io.Writer

interface ImmutableDbQueries : GenericDatabase {
    fun eventsToSync(lastEventSynced: Int): List<Event>
    fun eventCount(): Int
    fun backupToWriter(writer: PrintWriter)
}
