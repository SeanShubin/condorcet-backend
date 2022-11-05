package com.seanshubin.condorcet.backend.service

import com.seanshubin.condorcet.backend.crypto.OneWayHash

class OneWayHashStub(private val sample: Sample) : OneWayHash {
    override fun hash(bytes: ByteArray): ByteArray = sample.hash(bytes)
}

