package com.seanshubin.condorcet.backend.jwt

interface KeyLoader {
    fun publicKeyBytes():ByteArray
    fun privateKeyBytes():ByteArray
}