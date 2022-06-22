package com.seanshubin.condorcet.backend.domain

import java.time.*
import kotlin.test.Test
import kotlin.test.assertEquals

class TallyTest {
    @Test
    fun count() {
        val electionName = "Rock, Paper, Scissors"
        val createBallotsFunction = newCreateBallotsFunction()
        fun createBallots(quantity: Int, vararg candidate: String): List<RevealedBallot> =
            createBallotsFunction(quantity, candidate)

        val candidates = listOf("Rock", "Paper", "Scissors")
        val ballots =
            createBallots(4, "Rock", "Scissors", "Paper") +
                    createBallots(3, "Paper", "Rock", "Scissors") +
                    createBallots(3, "Scissors", "Paper", "Rock")
        val places = listOf(Place(1, "Rock"), Place(2, "Scissors"), Place(3, "Paper"))

        val secretBallot = false
        val tally = Tally.countBallots(electionName, secretBallot, candidates, ballots)
        assertEquals(places, tally.places)
    }

    @Test
    fun ties() {
        val electionName = "Ties"
        val createBallotsFunction = newCreateBallotsFunction()
        fun createBallots(quantity: Int, vararg candidate: String): List<RevealedBallot> =
            createBallotsFunction(quantity, candidate)

        val candidates = listOf("a", "b", "c", "d", "e")
        val ballots =
            createBallots(1, "a", "b") +
                    createBallots(1, "b", "c")
        val places = listOf(Place(1, "a"), Place(1, "b"), Place(3, "c"), Place(4, "d"), Place(4, "e"))

        val secretBallot = false
        val tally = Tally.countBallots(electionName, secretBallot, candidates, ballots)
        assertEquals(places, tally.places)
    }

    private fun newCreateBallotsFunction(): (Int, Array<out String>) -> List<RevealedBallot> =
        makeCreateRankingsFunction(UserRepository(), ConfirmationRepository(), ClockStub())

    private fun makeCreateRankingsFunction(
        userRepository: UserRepository,
        confirmationRepository: ConfirmationRepository,
        clockStub: ClockStub
    ): (Int, Array<out String>) -> List<RevealedBallot> {
        fun createRankings(quantity: Int, vararg candidates: String): List<RevealedBallot> {
            val rankings = candidates.mapIndexed { index, candidate -> Ranking(candidate, index + 1) }
                .sortedBy { it.candidateName }
            return (1..quantity).map {
                val user = userRepository.newUser()
                val election = "some election"
                val confirmation = confirmationRepository.newConfirmation()
                val whenCast = clockStub.instant()
                RevealedBallot(user, election, confirmation, whenCast, rankings)
            }
        }
        return ::createRankings
    }

    class UserRepository {
        var index = 0
        fun newUser(): String = "user-%02d".format(++index)
    }

    class ConfirmationRepository {
        var index = 0
        fun newConfirmation(): String = "confirmation-%02d".format(++index)
    }

    class ClockStub : Clock() {
        val utc = ZoneId.of("UTC")
        val baseDate = LocalDate.of(2022, 1, 1)
        val baseTime = LocalTime.of(0, 0, 0)
        val baseDateTime = ZonedDateTime.of(baseDate, baseTime, utc)
        var index = 0L
        override fun getZone(): ZoneId {
            throw UnsupportedOperationException("not implemented")
        }

        override fun withZone(zone: ZoneId?): Clock {
            throw UnsupportedOperationException("not implemented")
        }

        override fun instant(): Instant =
            baseDateTime.plusMinutes(++index).toInstant()
    }
}
