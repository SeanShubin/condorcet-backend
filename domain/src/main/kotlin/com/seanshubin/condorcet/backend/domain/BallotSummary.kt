package com.seanshubin.condorcet.backend.domain

import java.time.Instant

data class BallotSummary(
    val voterName: String,
    val electionName: String,
    val confirmation: String,
    val whenCast: Instant
)
