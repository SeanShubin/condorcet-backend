package com.seanshubin.condorcet.backend.domain

import com.seanshubin.condorcet.backend.domain.Ranking.Companion.effectiveRankings
import java.time.Instant

data class Ballot(
    val voterName: String,
    val electionName: String,
    val confirmation: String,
    val whenCast: Instant,
    val rankings: List<Ranking>
) {
    fun makeSecret():Ballot = copy(voterName="<redacted>")
    companion object{
        fun List<Ballot>.sortRankingsByName():List<Ballot> =
            map{ ballot -> ballot.copy(rankings = ballot.rankings.sortedBy { it.candidateName })}
        fun List<Ballot>.effectiveRankings(candidateNames:List<String>):List<Ballot> =
            map{ ballot -> ballot.copy(rankings = ballot.rankings.effectiveRankings(candidateNames).sortedBy { it.candidateName })}
    }
}
