package com.seanshubin.condorcet.backend.domain

import java.time.Instant

data class ElectionUpdates(
    val newName: String? = null,
    val secretBallot: Boolean? = null,
    val clearNoVotingBefore: Boolean? = null,
    val noVotingBefore: Instant? = null,
    val clearNoVotingAfter: Boolean? = null,
    val noVotingAfter: Instant? = null,
    val restrictWhoCanVote: Boolean? = null,
    val ownerCanDeleteBallots: Boolean? = null,
    val auditorCanDeleteBallots: Boolean? = null,
    val isTemplate: Boolean? = null,
    val allowVote: Boolean? = null,
    val allowEdit: Boolean? = null,
)
