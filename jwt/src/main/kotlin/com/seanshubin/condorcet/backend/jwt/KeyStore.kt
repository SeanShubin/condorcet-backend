package com.seanshubin.condorcet.backend.jwt

interface KeyStore {
    fun privateKey(): ByteArray
    fun publicKey():ByteArray
}
