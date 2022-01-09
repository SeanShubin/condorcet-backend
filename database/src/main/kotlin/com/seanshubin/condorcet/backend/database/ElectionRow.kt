package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.genericdb.DbRow
import java.time.Instant

data class ElectionRow(
    val owner: String,
    val name: String,
    val secretBallot: Boolean = true,
    val noVotingBefore: Instant? = null,
    val noVotingAfter: Instant? = null,
    val restrictWhoCanVote: Boolean = false,
    val ownerCanDeleteBallots: Boolean = false,
    val auditorCanDeleteBallots: Boolean = false,
    val isTemplate: Boolean = false,
    val allowChangesAfterVote: Boolean = false,
    val isOpen: Boolean = false,
    val candidateCount: Int
) : DbRow {
    override val cells: List<Any?> = listOf(
        owner,
        name,
        secretBallot,
        noVotingBefore,
        noVotingAfter,
        restrictWhoCanVote,
        ownerCanDeleteBallots,
        auditorCanDeleteBallots,
        isTemplate,
        allowChangesAfterVote,
        isOpen
    )
}
