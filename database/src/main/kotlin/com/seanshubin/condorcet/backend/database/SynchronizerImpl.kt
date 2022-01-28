package com.seanshubin.condorcet.backend.database

class SynchronizerImpl(
    private val immutableDbQueries: ImmutableDbQueries,
    private val mutableDbQueries: MutableDbQueries,
    private val mutableDbCommands: MutableDbCommands,
    private val eventCommandParser: EventCommandParser
) : Synchronizer {
    override tailrec fun synchronize() {
        val lastSynced = mutableDbQueries.lastSynced()
        if (lastSynced == null) {
            mutableDbCommands.initializeLastSynced(0)
            synchronize()
        } else {
            val eventsToSync = immutableDbQueries.eventsToSync(lastSynced)
            eventsToSync.forEach(::synchronizeEventRow)
        }
    }

    private fun synchronizeEventRow(event: Event) {
        val eventCommand = eventCommandParser.parse(event.type, event.text)
        eventCommand.exec(event.authority, mutableDbCommands)
        mutableDbCommands.setLastSynced(event.id)
    }
}
