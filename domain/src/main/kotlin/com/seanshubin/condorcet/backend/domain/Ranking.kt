package com.seanshubin.condorcet.backend.domain

import kotlin.random.Random

data class Ranking(val candidateName: String, val rank: Int?) {
    companion object{
        fun List<Ranking>.voterBiasedOrdering(random: Random):List<Ranking>{
            val rankAscending = Comparator<Ranking> { o1, o2 ->
                val rank1 = o1?.rank ?: Int.MAX_VALUE
                val rank2 = o2?.rank ?: Int.MAX_VALUE
                rank1.compareTo(rank2)
            }
            val grouped = groupBy { it.rank }
            val groupedValues = grouped.values
            val shuffled = groupedValues.flatMap { it.shuffled(random) }
            val sorted = shuffled.sortedWith(rankAscending)
            return sorted
        }

        fun List<Ranking>.addMissingCandidates(allCandidates: List<String>): List<Ranking> {
            val existingCandidates = this.map { it.candidateName }
            val isMissing = {candidate:String -> !existingCandidates.contains(candidate)}
            val missingCandidates = allCandidates.filter(isMissing)
            val newRankings = missingCandidates.map { Ranking(it, null)}
            return this + newRankings
        }
    }
}
