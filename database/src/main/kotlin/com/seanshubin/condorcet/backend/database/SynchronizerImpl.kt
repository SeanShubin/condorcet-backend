package com.seanshubin.condorcet.backend.database

class SynchronizerImpl(
    private val eventDbQueries: EventDbQueries,
    private val stateDbQueries: StateDbQueries,
    private val stateDbCommands: StateDbCommands,
    private val eventParser: DbEventParser
) : Synchronizer {
    override tailrec fun synchronize() {
        val lastSynced = stateDbQueries.lastSynced()
        if (lastSynced == null) {
            stateDbCommands.initializeLastSynced(0)
            synchronize()
        } else {
            val eventsToSync = eventDbQueries.eventsToSync(lastSynced)
            eventsToSync.forEach(::synchronizeEventRow)
        }
    }

    private fun synchronizeEventRow(eventRow: EventRow) {
        val event = eventParser.parse(eventRow.type, eventRow.text)
        event.exec(eventRow.authority, stateDbCommands)
        stateDbCommands.setLastSynced(eventRow.id)
    }
}
