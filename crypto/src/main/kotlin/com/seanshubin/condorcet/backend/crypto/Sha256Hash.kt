package com.seanshubin.condorcet.backend.crypto

import com.seanshubin.condorcet.backend.string.util.ByteArrayFormat
import java.security.MessageDigest

class Sha256Hash(val byteArrayFormat:ByteArrayFormat) : OneWayHash {
    companion object {
        private val messageDigest: MessageDigest by lazy {
            MessageDigest.getInstance("SHA-256")
        }
    }

    override fun hash(s: String): String {
        val inputBytes = s.toByteArray()
        val hashedBytes = messageDigest.digest(inputBytes)
        val hashedString = byteArrayFormat.encodeCompact(hashedBytes)
        return hashedString
    }
}
