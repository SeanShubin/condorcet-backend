package com.seanshubin.condorcet.backend.domain

import com.seanshubin.condorcet.backend.domain.Preference.Companion.toLines

data class Tally(
    val candidates: List<String>,
    val ballots: List<Ballot>,
    val preferences: List<List<Preference>>,
    val strongestPathMatrix: List<List<Preference>>,
    val places: List<Place>,
    val whoVoted: List<String>
) {
    fun toLines(): List<String> =
        listOf("candidates") +
                candidates.map(indent) +
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
}
