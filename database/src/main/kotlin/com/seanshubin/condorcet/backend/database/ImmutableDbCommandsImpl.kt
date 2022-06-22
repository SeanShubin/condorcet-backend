package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.genericdb.GenericDatabase
import java.time.Instant

class ImmutableDbCommandsImpl(
    genericDatabase: GenericDatabase,
    private val synchronizer: Synchronizer
) : ImmutableDbCommands, GenericDatabase by genericDatabase {
    override fun addEvent(authority: String, type: String, body: String, now: Instant) {
        update(
            "event-insert",
            now,
            authority,
            type,
            body
        )
        synchronizer.synchronize()
    }
}
