package com.seanshubin.condorcet.backend.database

class SynchronizerImpl(
    private val eventQueries: EventQueries,
    private val stateQueries: StateQueries,
    private val stateCommands: StateCommands,
    private val eventParser: EventParser
) : Synchronizer {
    override tailrec fun synchronize() {
        val lastSynced = stateQueries.lastSynced()
        if (lastSynced == null) {
            stateCommands.initializeLastSynced(0)
            synchronize()
        } else {
            val eventsToSync = eventQueries.eventsToSync(lastSynced)
            eventsToSync.forEach(::synchronizeEventRow)
        }
    }

    private fun synchronizeEventRow(eventRow: EventRow) {
        val event = eventParser.parse(eventRow.type, eventRow.text)
        event.exec(eventRow.authority, stateCommands)
        stateCommands.setLastSynced(eventRow.id)
    }
}
