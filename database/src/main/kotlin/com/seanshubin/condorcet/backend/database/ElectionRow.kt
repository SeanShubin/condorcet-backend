package com.seanshubin.condorcet.backend.database

import java.time.Instant

data class ElectionRow(
    val owner: String,
    val name: String,
    val secretBallot: Boolean = true,
    val noVotingBefore: Instant? = null,
    val noVotingAfter: Instant? = null,
    val allowEdit: Boolean = false,
    val allowVote: Boolean = false
)
