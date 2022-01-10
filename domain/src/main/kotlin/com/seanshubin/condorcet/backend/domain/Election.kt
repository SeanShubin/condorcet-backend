package com.seanshubin.condorcet.backend.domain

import java.time.Instant

data class Election(
    val ownerName: String,
    val name: String,
    val candidateCount: Int,
    val secretBallot: Boolean = true,
    val noVotingBefore: Instant? = null,
    val noVotingAfter: Instant? = null,
    val restrictWhoCanVote: Boolean = false,
    val ownerCanDeleteBallots: Boolean = false,
    val auditorCanDeleteBallots: Boolean = false,
    val isTemplate: Boolean = false,
    val allowEdit: Boolean = true,
    val allowVote: Boolean = false,
)
