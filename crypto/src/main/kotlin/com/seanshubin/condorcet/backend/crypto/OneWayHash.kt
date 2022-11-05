package com.seanshubin.condorcet.backend.crypto

interface OneWayHash {
    fun hash(bytes: ByteArray): ByteArray
}
