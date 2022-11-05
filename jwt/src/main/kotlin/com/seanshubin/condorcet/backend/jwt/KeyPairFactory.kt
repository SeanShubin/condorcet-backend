package com.seanshubin.condorcet.backend.jwt

interface KeyPairFactory {
    fun generateKeyPair(): KeyStore
}
