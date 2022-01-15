package com.seanshubin.condorcet.backend.domain

import com.seanshubin.condorcet.backend.domain.Ballot.Companion.sortRankingsByName
import com.seanshubin.condorcet.backend.domain.Preference.Companion.places
import com.seanshubin.condorcet.backend.domain.Preference.Companion.strongestPaths
import com.seanshubin.condorcet.backend.domain.Preference.Companion.toLines
import com.seanshubin.condorcet.backend.domain.Ranking.Companion.prefers

data class Tally(
    val candidateNames: List<String>,
    val secretBallot:Boolean,
    val ballots: List<Ballot>,
    val preferences: List<List<Preference>>,
    val strongestPathMatrix: List<List<Preference>>,
    val places: List<Place>,
    val whoVoted: List<String>
) {
    fun toLines(): List<String> =
        listOf("candidates") +
                candidateNames.map(indent) +
                listOf("ballots") +
                ballots.map { it.toString() }.map(indent) +
                listOf("preferences") +
                preferences.toLines().map(indent) +
                listOf("strongest paths") +
                strongestPathMatrix.toLines().map(indent) +
                listOf("places") +
                places.map { it.toString() }.map(indent) +
                listOf("who voted") +
                whoVoted.map(indent)

    private val indent = { s: String -> "  $s" }

    companion object {
        fun countBallots(secretBallot: Boolean, candidates: List<String>, ballots: List<Ballot>): Tally {
            return BallotCounter(secretBallot, candidates, ballots).countBallots()
        }

        class BallotCounter(val secretBallot: Boolean, val candidates: List<String>, val rawBallots: List<Ballot>) {
            fun countBallots(): Tally {
                val emptyPreferences = createEmptyPreferences()
                val preferences = rawBallots.map { it.rankings }.fold(emptyPreferences, ::accumulateRankings)
                val strongestPaths = preferences.strongestPaths()
                val places = strongestPaths.places(candidates)
                val whoVoted = rawBallots.map { it.userName }.sorted()
                val rankSortedBallots = rawBallots.sortRankingsByName()
                val ballots = if (secretBallot) {
                    rankSortedBallots.map { it.makeSecret() }.sortedBy { it.confirmation }
                } else {
                    rankSortedBallots.sortedBy { it.userName }
                }
                return Tally(candidates, secretBallot, ballots, preferences, strongestPaths, places, whoVoted)
            }

            fun createEmptyPreferences(): List<List<Preference>> =
                candidates.map { a ->
                    candidates.map { b ->
                        Preference(a, 0, b)
                    }
                }

            fun accumulateRankings(
                preferences: List<List<Preference>>,
                rankings: List<Ranking>
            ): List<List<Preference>> =
                preferences.indices.map { i ->
                    preferences.indices.map { j ->
                        val candidateA = candidates[i]
                        val candidateB = candidates[j]
                        val currentPreference = preferences[i][j]
                        if (rankings.prefers(candidateA, candidateB)) {
                            currentPreference.incrementStrength()
                        } else {
                            currentPreference
                        }
                    }
                }
        }

    }
}
