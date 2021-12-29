package com.seanshubin.condorcet.backend.domain

import com.seanshubin.condorcet.backend.domain.Preference.Companion.warshall
import kotlin.test.Test
import kotlin.test.assertEquals

class PreferenceTest {
    @Test
    fun create() {
        val a = Preference("x", 4, "y")
        assertEquals(4, a.strength)
        assertEquals("x", a.origin)
        assertEquals("y", a.destination)
        assertEquals("x-(4)-y", a.toString())
    }

    @Test
    fun addChain() {
        val a = Preference("x", 4, "y")
        val b = Preference("y", 5, "z")
        val c = a + b
        assertEquals(4, c.strength)
        assertEquals("x", c.origin)
        assertEquals("z", c.destination)
        assertEquals("x-(4)-y-(5)-z", c.toString())
    }

    @Test
    fun addStrength() {
        val a = Preference("x", 4, "y")
        val b = Preference("x", 5, "y")
        val c = a + b
        assertEquals(9, c.strength)
        assertEquals("x", c.origin)
        assertEquals("y", c.destination)
        assertEquals("x-(9)-y", c.toString())
    }

    @Test
    fun warshall() {
        val candidates = listOf("Rock", "Paper", "Scissors")
        val strengthMatrix = listOf(
            listOf(0, 7, 4),
            listOf(3, 0, 7),
            listOf(6, 3, 0)
        )
        val expected = listOf(
            listOf(
                Preference("Rock", 0, "Rock"),
                Preference("Rock", 7, "Paper"),
                Preference("Rock", 7, "Paper") + Preference("Paper", 7, "Scissors")
            ),
            listOf(
                Preference("Paper", 7, "Scissors") + Preference("Scissors", 6, "Rock"),
                Preference("Paper", 0, "Paper"),
                Preference("Paper", 7, "Scissors")
            ),
            listOf(
                Preference("Scissors", 6, "Rock"),
                Preference("Scissors", 6, "Rock") + Preference("Rock", 7, "Paper"),
                Preference("Scissors", 0, "Scissors")
            )
        )
        val preferenceMatrix = Preference.createPreferenceMatrix(candidates, strengthMatrix)
        val actual = preferenceMatrix.warshall()
        assertEquals(expected, actual)
    }
}