package com.seanshubin.condorcet.backend.crypto

import com.seanshubin.condorcet.backend.string.util.ByteArrayFormat
import java.security.SecureRandom

class SecureRandomIdGenerator(private val byteArrayFormat: ByteArrayFormat) : UniqueIdGenerator {
    override fun uniqueId(): String {
        val bytes = ByteArray(16)
        secureRandom.nextBytes(bytes)
        return byteArrayFormat.encodeCompact(bytes)
    }

    companion object {
        private val secureRandom: SecureRandom = SecureRandom()
    }
}
