package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.genericdb.GenericDatabase
import java.time.Clock

class EventDbCommandsImpl(
    genericDatabase: GenericDatabase,
    private val synchronizer: Synchronizer,
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
        synchronizer.synchronize()
    }
}
