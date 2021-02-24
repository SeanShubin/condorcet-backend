package com.seanshubin.condorcet.backend.domain

import java.time.Instant

data class ElectionUpdates(
    val restrictedToVoterList:Boolean? = null,
    val shouldSetStartTime:Boolean? = null,
    val startTime: Instant? = null,
    val shouldSetEndTime:Boolean? = null,
    val endTime: Instant? = null,
    val secretBallot: Boolean? = null,
    val shouldSetWhenDoneConfiguring:Boolean? = null,
    val whenDoneConfiguring: Instant?,
    val isTemplate: Boolean? = null,
    val isStarted: Boolean? = null,
    val isFinished: Boolean? = null,
    val canChangeCandidatesAfterDoneConfiguring: Boolean? = null,
    val ownerCanDeleteBallots: Boolean? = null,
    val auditorCanDeleteBallots: Boolean? = null
)