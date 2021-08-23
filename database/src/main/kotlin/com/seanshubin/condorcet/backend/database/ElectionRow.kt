package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.genericdb.DbRow
import java.time.Instant

data class ElectionRow(
    val owner: String,
    val name: String,
    val secretBallot: Boolean = true,
    val scheduledStart: Instant? = null,
    val scheduledEnd: Instant? = null,
    val restrictWhoCanVote: Boolean = false,
    val ownerCanDeleteBallots: Boolean = false,
    val auditorCanDeleteBallots: Boolean = false,
    val isTemplate: Boolean = false,
    val noChangesAfterVote: Boolean = false,
    val isOpen: Boolean = false,
    val candidateCount: Int
) : DbRow<String> {
    override val primaryKey: String = name
    override val cells: List<Any?> = listOf(
        owner,
        name,
        secretBallot,
        scheduledStart,
        scheduledEnd,
        restrictWhoCanVote,
        ownerCanDeleteBallots,
        auditorCanDeleteBallots,
        isTemplate,
        noChangesAfterVote,
        isOpen
    )
}
