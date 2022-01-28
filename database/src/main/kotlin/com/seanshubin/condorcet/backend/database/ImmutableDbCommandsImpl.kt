package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.genericdb.GenericDatabase
import java.time.Clock

class ImmutableDbCommandsImpl(
    genericDatabase: GenericDatabase,
    private val synchronizer: Synchronizer,
    private val clock: Clock
) : ImmutableDbCommands, GenericDatabase by genericDatabase {
    override fun addEvent(authority: String, type: String, body: String) {
        update(
            "event-insert",
            clock.instant(),
            authority,
            type,
            body
        )
        synchronizer.synchronize()
    }
}
