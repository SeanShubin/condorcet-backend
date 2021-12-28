package com.seanshubin.condorcet.backend.domain

import com.seanshubin.condorcet.backend.domain.Ranking.Companion.addMissingCandidates
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
        val random = RandomStub(1,1)
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
        val allCandidates = listOf("Cat", "Dog", "Fish", "Bird","Reptile")
        val original = listOf(fish, bird)
        val expected = listOf(fish, bird, cat, dog, reptile)
        val actual = original.addMissingCandidates(allCandidates)
        assertEquals(expected, actual)
    }

    class RandomStub(vararg val numbers:Int): Random(){
        var index = 0
        override fun nextBits(bitCount: Int): Int {
            throw UnsupportedOperationException("not implemented")
        }

        override fun nextInt(from: Int, until: Int): Int {
            return numbers[index++]
        }
    }
}
