package com.seanshubin.condorcet.backend.crypto

interface OneWayHash {
    fun hash(s: String): String
}
