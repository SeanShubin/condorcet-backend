package com.seanshubin.condorcet.backend.database

import java.time.Instant

data class Event(
    val id: Int,
    val whenHappened: Instant,
    val authority: String,
    val type: String,
    val text: String
)
