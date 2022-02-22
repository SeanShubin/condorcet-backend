package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.genericdb.GenericDatabase
import java.io.BufferedWriter
import java.io.PrintWriter
import java.io.Writer
import java.sql.ResultSet

class ImmutableDbQueriesImpl(genericDatabase: GenericDatabase) : ImmutableDbQueries, GenericDatabase by genericDatabase {
    override fun eventsToSync(lastEventSynced: Int): List<Event> =
        query(::createEvent, "event-select-unsynced", lastEventSynced)

    override fun eventCount(): Int =
        queryExactlyOneInt("event-count")

    override fun backupToWriter(writer: PrintWriter) {
        fun emitEvent(event:Event) {
            writer.println(event.toLine())
        }
        queryStreaming(::createEvent, ::emitEvent, "event-select")
    }

    private fun createEvent(resultSet: ResultSet): Event {
        val id = resultSet.getInt("id")
        val whenHappened = resultSet.getTimestamp("when").toInstant()
        val authority = resultSet.getString("authority")
        val type = resultSet.getString("type")
        val text = resultSet.getString("text")
        return Event(id, whenHappened, authority, type, text)
    }

}
