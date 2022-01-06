package com.seanshubin.condorcet.backend.domain

import java.time.Instant

data class Ballot(
    val user: String,
    val election: String,
    val confirmation: String,
    val whenCast: Instant,
    val rankings: List<Ranking>
)
