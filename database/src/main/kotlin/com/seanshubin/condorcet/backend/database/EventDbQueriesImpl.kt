package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.genericdb.GenericDatabase
import java.sql.ResultSet

class EventDbQueriesImpl(genericDatabase: GenericDatabase) : EventDbQueries, GenericDatabase by genericDatabase {
    override fun eventsToSync(lastEventSynced: Int): List<EventRow> =
        query(::createEvent, "list-unsynced-events", lastEventSynced)

    override fun eventCount(): Int =
        queryExactlyOneInt("count-events")

    private fun createEvent(resultSet: ResultSet): EventRow {
        val id = resultSet.getInt("id")
        val whenHappened = resultSet.getTimestamp("when").toInstant()
        val authority = resultSet.getString("authority")
        val type = resultSet.getString("type")
        val text = resultSet.getString("text")
        return EventRow(id, whenHappened, authority, type, text)
    }

}
