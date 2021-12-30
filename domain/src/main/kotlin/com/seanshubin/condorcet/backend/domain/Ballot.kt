package com.seanshubin.condorcet.backend.domain

import com.seanshubin.condorcet.backend.domain.Preference.Companion.places
import com.seanshubin.condorcet.backend.domain.Preference.Companion.strongestPaths

data class Ballot(val rankings: List<Ranking>) {
    fun prefers(a: String, b: String): Boolean {
        return rankingFor(a) < rankingFor(b)
    }

    private fun rankingFor(name: String): Int =
        rankings.find { ranking -> ranking.candidateName == name }?.rank ?: Int.MAX_VALUE

    override fun toString(): String =
        rankings.joinToString(" ") { (candidateName, rank) -> "$rank $candidateName" }

    companion object {
        fun List<Ballot>.tally(candidates: List<String>): Tally {
            return BallotCounter(candidates, this).countBallots()
        }

        class BallotCounter(val candidates: List<String>, val ballots: List<Ballot>) {
            fun countBallots(): Tally {
                val emptyPreferences = createEmptyPreferences()
                val preferences = ballots.fold(emptyPreferences, ::accumulateBallot)
                val strongestPaths = preferences.strongestPaths()
                val places = strongestPaths.places(candidates)
                return Tally(candidates, ballots, preferences, strongestPaths, places)
            }

            fun createEmptyPreferences(): List<List<Preference>> =
                candidates.map { a ->
                    candidates.map { b ->
                        Preference(a, 0, b)
                    }
                }

            fun accumulateBallot(preferences: List<List<Preference>>, ballot: Ballot): List<List<Preference>> =
                preferences.indices.map { i ->
                    preferences.indices.map { j ->
                        val candidateA = candidates[i]
                        val candidateB = candidates[j]
                        val currentPreference = preferences[i][j]
                        if (ballot.prefers(candidateA, candidateB)) {
                            currentPreference.incrementStrength()
                        } else {
                            currentPreference
                        }
                    }
                }
        }
    }
}
