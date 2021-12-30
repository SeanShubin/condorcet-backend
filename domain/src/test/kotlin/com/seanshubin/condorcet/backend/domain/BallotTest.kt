package com.seanshubin.condorcet.backend.domain

import com.seanshubin.condorcet.backend.domain.Ballot.Companion.tally
import kotlin.test.Test

class BallotTest {
    @Test
    fun count() {
        val candidates = listOf("Rock", "Paper", "Scissors")
        val ballots =
            createBallots(4, "Rock", "Scissors", "Paper") +
                    createBallots(3, "Paper", "Rock", "Scissors") +
                    createBallots(3, "Scissors", "Paper", "Rock")
        val tally = ballots.tally(candidates)
        tally.toLines().forEach(::println)
    }

    @Test
    fun ties() {
        val candidates = listOf("a", "b", "c", "d", "e")
        val ballots =
            createBallots(1, "a", "b") +
                    createBallots(1, "b", "c")
        val tally = ballots.tally(candidates)
        tally.toLines().forEach(::println)
    }

    private fun createBallots(quantity: Int, vararg candidates: String): List<Ballot> {
        val rankings = candidates.mapIndexed { index, candidate -> Ranking(candidate, index + 1) }
        return (1..quantity).map { MyBallot(rankings) }
    }

    data class MyBallot(val rankings: List<Ranking>) : Ballot {
        override fun prefers(a: String, b: String): Boolean {
            return rankingFor(a) < rankingFor(b)
        }

        override fun toString(): String =
            rankings.joinToString(" ") { (candidateName, rank) -> "$rank $candidateName" }

        private fun rankingFor(name: String): Int =
            rankings.find { ranking -> ranking.candidateName == name }?.rank ?: Int.MAX_VALUE
    }
}