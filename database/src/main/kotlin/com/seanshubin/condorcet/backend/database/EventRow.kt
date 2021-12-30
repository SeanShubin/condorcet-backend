package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.genericdb.DbRow
import java.time.Instant

data class EventRow(
    val id: Int,
    val whenHappened: Instant,
    val authority: String,
    val type: String,
    val text: String
) : DbRow {
    override val cells: List<Any?>
        get() = listOf(id, whenHappened, authority, type, text)
}
