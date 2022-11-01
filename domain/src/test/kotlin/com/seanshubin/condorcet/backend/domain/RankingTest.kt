package com.seanshubin.condorcet.backend.domain

import com.seanshubin.condorcet.backend.domain.Ranking.Companion.addMissingCandidates
import com.seanshubin.condorcet.backend.domain.Ranking.Companion.normalizeRankingsReplaceNulls
import com.seanshubin.condorcet.backend.domain.Ranking.Companion.normalizeRankingsKeepNulls
import com.seanshubin.condorcet.backend.domain.Ranking.Companion.voterBiasedOrdering
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

class RankingTest {
    @Test
    fun sortOrder() {
        val c = Ranking("c", 1)
        val o = Ranking("o", 2)
        val m = Ranking("m", 3)
        val p = Ranking("p", 4)
        val a = Ranking("a", null)
        val r = Ranking("r", null)
        val e = Ranking("e", null)
        val original = listOf(a, c, e, m, o, p, r)
        val expected = listOf(c, o, m, p, a, r, e)
        // nulls are going to be at: a e r
        // shuffle builds from end to beginning
        // so grab the e first at 1, leaving: a r
        // then grab the r, now also 1
        val random = RandomStub(1, 1)
        val actual = original.voterBiasedOrdering(random)
        assertEquals(expected, actual)
    }

    @Test
    fun addMissingCandidates() {
        val fish = Ranking("Fish", 1)
        val bird = Ranking("Bird", 2)
        val cat = Ranking("Cat", null)
        val dog = Ranking("Dog", null)
        val reptile = Ranking("Reptile", null)
        val allCandidates = listOf("Cat", "Dog", "Fish", "Bird", "Reptile")
        val original = listOf(fish, bird)
        val expected = listOf(fish, bird, cat, dog, reptile)
        val actual = original.addMissingCandidates(allCandidates)
        assertEquals(expected, actual)
    }

    @Test
    fun normalizeRankingsKeepNulls() {
        val a = Ranking("a", -4)
        val b = Ranking("b", 9)
        val c = Ranking("c", 2)
        val d = Ranking("d", 2)
        val e = Ranking("e", 3)
        val f = Ranking("f", null)
        val g = Ranking("g", 5)
        val h = Ranking("h", null)
        val i = Ranking("i", 0)
        val j = Ranking("j", 2)
        val input = listOf(a, b, c, d, e, f, g, h, i, j)
        val actual = input.normalizeRankingsKeepNulls()
        val expected = listOf(
            Ranking("a", 1),
            Ranking("b", 6),
            Ranking("c", 3),
            Ranking("d", 3),
            Ranking("e", 4),
            Ranking("f", null),
            Ranking("g", 5),
            Ranking("h", null),
            Ranking("i", 2),
            Ranking("j", 3)
        )
        assertEquals(expected, actual)
    }

    @Test
    fun normalizeRankingsReplaceNulls() {
        val a = Ranking("a", -4)
        val b = Ranking("b", 9)
        val c = Ranking("c", 2)
        val d = Ranking("d", 2)
        val e = Ranking("e", 3)
        val f = Ranking("f", null)
        val g = Ranking("g", 5)
        val h = Ranking("h", null)
        val i = Ranking("i", 0)
        val j = Ranking("j", 2)
        val input = listOf(a, b, c, d, e, f, g, h, i, j)
        val actual = input.normalizeRankingsReplaceNulls()
        val expected = listOf(
            Ranking("a", 1),
            Ranking("b", 6),
            Ranking("c", 3),
            Ranking("d", 3),
            Ranking("e", 4),
            Ranking("f", 7),
            Ranking("g", 5),
            Ranking("h", 7),
            Ranking("i", 2),
            Ranking("j", 3)
        )
        assertEquals(expected, actual)
    }

    class RandomStub(vararg val numbers: Int) : Random() {
        var index = 0
        override fun nextBits(bitCount: Int): Int {
            throw UnsupportedOperationException("not implemented")
        }

        override fun nextInt(from: Int, until: Int): Int {
            return numbers[index++]
        }
    }
}
