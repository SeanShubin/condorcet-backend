package com.seanshubin.condorcet.backend.domain

import com.seanshubin.condorcet.backend.domain.Preference.Companion.places
import com.seanshubin.condorcet.backend.domain.Preference.Companion.strongestPaths
import com.seanshubin.condorcet.backend.domain.Ranking.Companion.prefers
import java.time.Instant

data class Ballot(val user:String,
                  val election:String,
                  val confirmation:String,
                  val whenCast: Instant,
                  val rankings: List<Ranking>) {
    companion object {
        fun List<Ballot>.tally(candidates: List<String>): Tally {
            return BallotCounter(candidates, this).countBallots()
        }

        class BallotCounter(val candidates: List<String>, val ballots: List<Ballot>) {
            fun countBallots(): Tally {
                val emptyPreferences = createEmptyPreferences()
                val preferences = ballots.map{it.rankings}.fold(emptyPreferences, ::accumulateRankings)
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

            fun accumulateRankings(preferences: List<List<Preference>>, rankings: List<Ranking>): List<List<Preference>> =
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
