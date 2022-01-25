package com.seanshubin.condorcet.backend.domain

import java.time.Instant

data class ElectionUpdates(
    val newElectionName: String? = null,
    val secretBallot: Boolean? = null,
    val clearNoVotingBefore: Boolean? = null,
    val noVotingBefore: Instant? = null,
    val clearNoVotingAfter: Boolean? = null,
    val noVotingAfter: Instant? = null,
    val allowVote: Boolean? = null,
    val allowEdit: Boolean? = null
)
