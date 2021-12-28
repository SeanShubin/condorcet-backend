package com.seanshubin.condorcet.backend.service

import kotlin.random.Random

class RandomStub: Random() {
    override fun nextBits(bitCount: Int): Int {
        throw UnsupportedOperationException("not implemented")
    }
}