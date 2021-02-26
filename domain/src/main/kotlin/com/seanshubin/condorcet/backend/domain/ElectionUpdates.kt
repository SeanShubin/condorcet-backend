package com.seanshubin.condorcet.backend.domain

import java.time.Instant

data class ElectionUpdates(
    val newName: String? = null,
    val secretBallot: Boolean? = null,
    val clearScheduledStart: Boolean? = null,
    val scheduledStart: Instant? = null,
    val clearScheduledEnd: Boolean? = null,
    val scheduledEnd: Instant? = null,
    val restrictWhoCanVote: Boolean? = null,
    val ownerCanDeleteBallots: Boolean? = null,
    val auditorCanDeleteBallots: Boolean? = null,
    val isTemplate: Boolean? = null,
    val noMoreChanges: Boolean? = null,
    val isOpen: Boolean? = null,
)
