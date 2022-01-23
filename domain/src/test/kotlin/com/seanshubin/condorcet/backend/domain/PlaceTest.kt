package com.seanshubin.condorcet.backend.domain

import com.seanshubin.condorcet.backend.domain.Place.Companion.adjustForTies
import kotlin.test.Test
import kotlin.test.assertEquals

class PlaceTest {
    @Test
    fun adjustForTies(){
        // given
        val places = listOf(
            Place(1, "a"),
            Place(1, "b"),
            Place(2, "c"),
            Place(3, "d"),
            Place(3, "e"),
            Place(4, "f"),
        )
        val expected = listOf(
            Place(1, "a"),
            Place(1, "b"),
            Place(3, "c"),
            Place(4, "d"),
            Place(4, "e"),
            Place(6, "f"),
        )

        // when
        val actual = places.adjustForTies()

        // then
        assertEquals(expected, actual)
    }

    @Test
    fun adjustForThreeWayTie(){
        // given
        val places = listOf(
            Place(1, "a"),
            Place(2, "b"),
            Place(2, "c"),
            Place(2, "d"),
            Place(3, "e")
        )
        val expected = listOf(
            Place(1, "a"),
            Place(2, "b"),
            Place(2, "c"),
            Place(2, "d"),
            Place(5, "e")
        )

        // when
        val actual = places.adjustForTies()

        // then
        assertEquals(expected, actual)
    }
}
