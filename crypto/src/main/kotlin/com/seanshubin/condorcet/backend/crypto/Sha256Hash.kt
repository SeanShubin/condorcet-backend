package com.seanshubin.condorcet.backend.crypto

import com.seanshubin.condorcet.backend.string.util.HexFormat.toCompactHex
import java.security.MessageDigest

class Sha256Hash : OneWayHash {
    companion object {
        private val messageDigest: MessageDigest by lazy {
            MessageDigest.getInstance("SHA-256")
        }
    }

    override fun hash(s: String): String {
        val inputBytes = s.toByteArray()
        val hashedBytes = messageDigest.digest(inputBytes)
        val hashedString = hashedBytes.toCompactHex()
        return hashedString
    }
}
