package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.genericdb.GenericDatabase
import java.time.Clock

class EventDbCommandsImpl(
    genericDatabase: GenericDatabase,
    private val eventDbQueries: EventDbQueries,
    private val stateDbCommands: StateDbCommands,
    private val eventParser: DbEventParser,
    private val clock: Clock
) : EventDbCommands, GenericDatabase by genericDatabase {
    override fun addEvent(authority: String, type: String, body: String) {
        update(
            "insert-event",
            clock.instant(),
            authority,
            type,
            body
        )
        synchronize()
    }

    override fun setLastSynced(lastSynced: Int) {
        update("set-last-synced", lastSynced)
    }

    override fun initializeLastSynced(lastSynced: Int) {
        update("initialize-last-synced", lastSynced)
    }

    private tailrec fun synchronize() {
        val lastSynced = eventDbQueries.lastSynced()
        if (lastSynced == null) {
            initializeLastSynced(0)
            synchronize()
        } else {
            val eventsToSync = eventDbQueries.eventsToSync(lastSynced)
            eventsToSync.forEach(::synchronizeEventRow)
        }
    }

    private fun synchronizeEventRow(eventRow: EventRow) {
        val event = eventParser.parse(eventRow.type, eventRow.text)
        event.exec(eventRow.authority, stateDbCommands)
        setLastSynced(eventRow.id)
    }
}
