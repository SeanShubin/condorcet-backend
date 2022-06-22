package com.seanshubin.condorcet.backend.crypto

import com.seanshubin.condorcet.backend.string.util.HexFormat.toCompactHex
import java.security.SecureRandom

class SecureRandomIdGenerator : UniqueIdGenerator {
    override fun uniqueId(): String {
        val bytes = ByteArray(16)
        secureRandom.nextBytes(bytes)
        return bytes.toCompactHex()
    }

    companion object {
        private val secureRandom: SecureRandom = SecureRandom()
    }
}
