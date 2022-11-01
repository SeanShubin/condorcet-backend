package com.seanshubin.condorcet.backend.jwt

import java.security.KeyPairGenerator

class KeyPairFactoryImpl : KeyPairFactory {
    override fun generateKeyPair(): KeyStore {
        val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
        keyPairGenerator.initialize(1024)
        val keyPair = keyPairGenerator.generateKeyPair()
        return object : KeyStore {
            override fun privateKey(): ByteArray = keyPair.private.encoded

            override fun publicKey(): ByteArray = keyPair.public.encoded
        }
    }
}