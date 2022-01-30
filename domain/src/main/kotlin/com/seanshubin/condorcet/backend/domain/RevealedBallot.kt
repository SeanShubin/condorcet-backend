package com.seanshubin.condorcet.backend.domain

import com.seanshubin.condorcet.backend.domain.Ranking.Companion.effectiveRankings
import com.seanshubin.condorcet.backend.domain.Ranking.Companion.matchOrderToCandidates
import java.time.Instant

data class RevealedBallot(
    val voterName: String,
    val electionName: String,
    val confirmation: String,
    val whenCast: Instant,
    val rankings: List<Ranking>
):Ballot {
    fun makeSecret():SecretBallot = SecretBallot(electionName, confirmation, rankings)
    companion object{
        fun List<RevealedBallot>.matchRankingsOrderToCandidates(candidateNames:List<String>):List<RevealedBallot> =
            map{ ballot -> ballot.copy(rankings = ballot.rankings.matchOrderToCandidates(candidateNames))}
        fun List<RevealedBallot>.effectiveRankings(candidateNames:List<String>):List<RevealedBallot> =
            map{ ballot -> ballot.copy(rankings = ballot.rankings.effectiveRankings(candidateNames).sortedBy { it.candidateName })}
    }
}
