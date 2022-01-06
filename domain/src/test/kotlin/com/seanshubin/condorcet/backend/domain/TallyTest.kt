package com.seanshubin.condorcet.backend.domain

import java.time.*
import kotlin.test.Test
import kotlin.test.assertEquals

class TallyTest {
    @Test
    fun count() {
        val createBallotsFunction = newCreateBallotsFunction()
        fun createBallots(quantity: Int, vararg candidate: String): List<Ballot> =
            createBallotsFunction(quantity, candidate)

        val candidates = listOf("Rock", "Paper", "Scissors")
        val ballots =
            createBallots(4, "Rock", "Scissors", "Paper") +
                    createBallots(3, "Paper", "Rock", "Scissors") +
                    createBallots(3, "Scissors", "Paper", "Rock")
        val preferences = listOf(
            listOf(Preference("Rock", 0, "Rock"), Preference("Rock", 4, "Paper"), Preference("Rock", 7, "Scissors")),
            listOf(Preference("Paper", 6, "Rock"), Preference("Paper", 0, "Paper"), Preference("Paper", 3, "Scissors")),
            listOf(
                Preference("Scissors", 3, "Rock"),
                Preference("Scissors", 7, "Paper"),
                Preference("Scissors", 0, "Scissors")
            )
        )
        val strongestPathMatrix = listOf(
            listOf(
                Preference("Rock", 0, "Rock"),
                Preference("Rock", 7, "Scissors") + Preference("Scissors", 7, "Paper"),
                Preference("Rock", 7, "Scissors")
            ),
            listOf(
                Preference("Paper", 6, "Rock"),
                Preference("Paper", 0, "Paper"),
                Preference("Paper", 6, "Rock") + Preference("Rock", 7, "Scissors")
            ),
            listOf(
                Preference("Scissors", 7, "Paper") + Preference("Paper", 6, "Rock"),
                Preference("Scissors", 7, "Paper"),
                Preference("Scissors", 0, "Scissors")
            )
        )
        val places = listOf(Place(1, "Rock"), Place(2, "Scissors"), Place(3, "Paper"))

        val tally = Tally.countBallots(candidates, ballots)
        assertEquals(candidates, tally.candidates)
        assertEquals(ballots, tally.ballots)
        assertEquals(preferences, tally.preferences)
        assertEquals(strongestPathMatrix, tally.strongestPathMatrix)
        assertEquals(places, tally.places)
    }

    @Test
    fun ties() {
        val createBallotsFunction = newCreateBallotsFunction()
        fun createBallots(quantity:Int, vararg candidate:String):List<Ballot> = createBallotsFunction(quantity, candidate)
        val candidates = listOf("a", "b", "c", "d", "e")
        val ballots =
            createBallots(1, "a", "b") +
                    createBallots(1, "b", "c")
        val preferences = listOf(
            listOf(
                Preference("a", 0, "a"),
                Preference("a", 1, "b"),
                Preference("a", 1, "c"),
                Preference("a", 1, "d"),
                Preference("a", 1, "e")
            ),
            listOf(
                Preference("b", 1, "a"),
                Preference("b", 0, "b"),
                Preference("b", 2, "c"),
                Preference("b", 2, "d"),
                Preference("b", 2, "e")
            ),
            listOf(
                Preference("c", 1, "a"),
                Preference("c", 0, "b"),
                Preference("c", 0, "c"),
                Preference("c", 1, "d"),
                Preference("c", 1, "e")
            ),
            listOf(
                Preference("d", 0, "a"),
                Preference("d", 0, "b"),
                Preference("d", 0, "c"),
                Preference("d", 0, "d"),
                Preference("d", 0, "e")
            ),
            listOf(
                Preference("e", 0, "a"),
                Preference("e", 0, "b"),
                Preference("e", 0, "c"),
                Preference("e", 0, "d"),
                Preference("e", 0, "e")
            )
        )
        val strongestPathMatrix = listOf(
            listOf(
                Preference("a", 0, "a"),
                Preference("a", 1, "b"),
                Preference("a", 1, "c"),
                Preference("a", 1, "d"),
                Preference("a", 1, "e")
            ),
            listOf(
                Preference("b", 1, "a"),
                Preference("b", 0, "b"),
                Preference("b", 2, "c"),
                Preference("b", 2, "d"),
                Preference("b", 2, "e")
            ),
            listOf(
                Preference("c", 1, "a"),
                Preference("c", 1, "a") + Preference("a", 1, "b"),
                Preference("c", 0, "c"),
                Preference("c", 1, "d"),
                Preference("c", 1, "e")
            ),
            listOf(
                Preference("d", 0, "a"),
                Preference("d", 0, "b"),
                Preference("d", 0, "c"),
                Preference("d", 0, "d"),
                Preference("d", 0, "e")
            ),
            listOf(
                Preference("e", 0, "a"),
                Preference("e", 0, "b"),
                Preference("e", 0, "c"),
                Preference("e", 0, "d"),
                Preference("e", 0, "e")
            )
        )
        val places = listOf(Place(1, "a"), Place(1, "b"), Place(3, "c"), Place(4, "d"), Place(4, "e"))

        val tally = Tally.countBallots(candidates, ballots)
        assertEquals(candidates, tally.candidates)
        assertEquals(ballots, tally.ballots)
        assertEquals(preferences, tally.preferences)
        assertEquals(strongestPathMatrix, tally.strongestPathMatrix)
        assertEquals(places, tally.places)
    }

    private fun newCreateBallotsFunction():(Int, Array<out String>) -> List<Ballot> =
        makeCreateRankingsFunction(UserRepository(), ConfirmationRepository(), ClockStub())

    private fun makeCreateRankingsFunction(
        userRepository: UserRepository,
        confirmationRepository: ConfirmationRepository,
        clockStub: ClockStub
    ): (Int, Array<out String>) -> List<Ballot> {
        fun createRankings(quantity: Int, vararg candidates: String): List<Ballot> {
            val rankings = candidates.mapIndexed { index, candidate -> Ranking(candidate, index + 1) }
            return (1..quantity).map {
                val user = userRepository.newUser()
                val election = "some election"
                val confirmation = confirmationRepository.newConfirmation()
                val whenCast = clockStub.instant()
                Ballot(user, election, confirmation, whenCast, rankings)
            }
        }
        return ::createRankings
    }

    private fun createRankingsBaseFunction(
        userRepository: UserRepository,
        confirmationRepository: ConfirmationRepository,
        clockStub: ClockStub,
        quantity: Int,
        vararg candidates: String
    ): List<Ballot> {
        val rankings = candidates.mapIndexed { index, candidate -> Ranking(candidate, index + 1) }
        return (1..quantity).map {
            val user = userRepository.newUser()
            val election = "some election"
            val confirmation = confirmationRepository.newConfirmation()
            val whenCast = clockStub.instant()
            Ballot(user, election, confirmation, whenCast, rankings)
        }
    }

    class UserRepository {
        var index = 0
        fun newUser(): String = "user-${++index}"
    }

    class ConfirmationRepository {
        var index = 0
        fun newConfirmation(): String = "confirmation-${++index}"
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
