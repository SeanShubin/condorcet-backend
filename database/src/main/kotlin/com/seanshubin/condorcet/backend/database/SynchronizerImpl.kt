package com.seanshubin.condorcet.backend.database

class SynchronizerImpl(
    private val eventQueries: EventQueries,
    private val stateQueries: StateQueries,
    private val stateCommands: StateCommands,
    private val eventCommandParser: EventCommandParser
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

    private fun synchronizeEventRow(event: Event) {
        val eventCommand = eventCommandParser.parse(event.type, event.text)
        eventCommand.exec(event.authority, stateCommands)
        stateCommands.setLastSynced(event.id)
    }
}
