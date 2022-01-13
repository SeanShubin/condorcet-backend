package com.seanshubin.condorcet.backend.domain

import java.time.Instant

data class ElectionUpdates(
    val newName: String? = null,
    val secretBallot: Boolean? = null,
    val clearNoVotingBefore: Boolean? = null,
    val noVotingBefore: Instant? = null,
    val clearNoVotingAfter: Boolean? = null,
    val noVotingAfter: Instant? = null
)
