package com.seanshubin.condorcet.backend.genericdb

import java.time.Instant

data class EventRow(
    val id: Int,
    val type: String,
    val whenHappened: Instant,
    val text: String
) : DbRow<Int> {
    override val primaryKey: Int
        get() = id
    override val cells: List<Any?>
        get() = listOf(id, type, whenHappened, text)
}
