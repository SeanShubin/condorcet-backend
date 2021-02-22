package com.seanshubin.condorcet.backend.domain

import java.time.Instant

data class Election(
    val ownerName: String,
    val name: String,
    val startTime: Instant? = null,
    val endTime: Instant? = null,
    val secretBallot: Boolean = true,
    val whenDoneConfiguring: Instant?,
    val isTemplate: Boolean = false,
    val isStarted: Boolean = false,
    val isFinished: Boolean = false,
    val canChangeCandidatesAfterDoneConfiguring: Boolean = false,
    val creatorCanDeleteBallots: Boolean = false,
    val auditorCanDeleteBallots: Boolean = false
)
