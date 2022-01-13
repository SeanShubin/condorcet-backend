package com.seanshubin.condorcet.backend.domain

import java.time.Instant

data class ElectionDetail(
    val ownerName: String,
    val name: String,
    val candidateCount: Int,
    val voterCount:Int,
    val secretBallot: Boolean = true,
    val noVotingBefore: Instant? = null,
    val noVotingAfter: Instant? = null,
    val allowEdit: Boolean = true,
    val allowVote: Boolean = false,
)
