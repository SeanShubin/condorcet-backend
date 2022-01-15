package com.seanshubin.condorcet.backend.domain

import java.time.Instant

data class BallotSummary(
    val userName: String,
    val electionName: String,
    val confirmation: String,
    val whenCast: Instant
)
