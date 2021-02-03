package com.seanshubin.condorcet.backend.genericdb

import java.sql.ResultSet

class EventDbQueriesImpl(genericDatabase: GenericDatabase) : EventDbQueries, GenericDatabase by genericDatabase {
    override fun eventsToSync(lastEventSynced: Int): List<EventRow> =
        query(::createEvent, "list-unsynced-events", lastEventSynced)

    override fun lastSynced(): Int? = queryZeroOrOneInt("get-last-synced")

    private fun createEvent(resultSet: ResultSet): EventRow {
        val id = resultSet.getInt("id")
        val type = resultSet.getString("type")
        val whenHappened = resultSet.getTimestamp("when").toInstant()
        val text = resultSet.getString("text")
        return EventRow(id, type, whenHappened, text)
    }

}
