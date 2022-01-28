package com.seanshubin.condorcet.backend.database

import java.time.Instant

interface ImmutableDbCommands {
    fun addEvent(authority: String, type: String, body: String, now: Instant)
}
