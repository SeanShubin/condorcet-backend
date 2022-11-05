package com.seanshubin.condorcet.backend.crypto

import java.security.SecureRandom

object SecureRandomIdGenerator : UniqueIdGenerator {
    private val secureRandom: SecureRandom = SecureRandom()

    override fun uniqueId(): ByteArray {
        val bytes = ByteArray(32)
        secureRandom.nextBytes(bytes)
        return bytes
    }
}
