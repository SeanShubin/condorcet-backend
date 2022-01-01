package com.seanshubin.condorcet.backend.domain

import java.time.Instant

class BallotRanking(
    val user: String,
    val election: String,
    val confirmation: String,
    val whenCast: Instant,
    val rankings: List<Ranking>
)
