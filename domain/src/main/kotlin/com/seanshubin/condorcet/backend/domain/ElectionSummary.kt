package com.seanshubin.condorcet.backend.domain

import java.time.Instant

data class ElectionSummary(
    val ownerName: String,
    val name: String,
    val secretBallot: Boolean = true,
    val noVotingBefore: Instant? = null,
    val noVotingAfter: Instant? = null,
    val allowEdit: Boolean = true,
    val allowVote: Boolean = false,
)
