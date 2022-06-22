package com.seanshubin.condorcet.backend.domain

data class SecretBallot(
    val electionName: String,
    val confirmation: String,
    override val rankings: List<Ranking>
) : Ballot
