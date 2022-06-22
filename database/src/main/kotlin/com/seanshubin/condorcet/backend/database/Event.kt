package com.seanshubin.condorcet.backend.database

import java.time.Instant

data class Event(
    val id: Int,
    val whenHappened: Instant,
    val authority: String,
    val type: String,
    val text: String
) {
    fun toLine(): String = toRow().joinToString("\t")

    private fun toRow(): List<Any> = listOf(id, whenHappened, authority, type, text)

    companion object {
        fun fromLine(line: String): Event {
            val parts = line.split('\t')
            val id = parts[0].toInt()
            val whenHappened = Instant.parse(parts[1])
            val authority = parts[2]
            val type = parts[3]
            val text = parts[4]
            return Event(id, whenHappened, authority, type, text)
        }
    }
}
