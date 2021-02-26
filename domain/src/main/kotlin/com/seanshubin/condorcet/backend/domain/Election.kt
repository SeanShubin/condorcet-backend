package com.seanshubin.condorcet.backend.domain

import java.time.Instant

data class Election(
    val ownerName: String,
    val name: String,
    val secretBallot: Boolean = true,
    val scheduledStart: Instant? = null,
    val scheduledEnd: Instant? = null,
    val restrictWhoCanVote: Boolean = false,
    val ownerCanDeleteBallots: Boolean = false,
    val auditorCanDeleteBallots: Boolean = false,
    val isTemplate: Boolean = false,
    val noMoreChanges: Boolean = false,
    val isOpen: Boolean = false
)
