package com.seanshubin.condorcet.backend.crypto

import java.security.MessageDigest

object Sha256Hash : OneWayHash {
    private val messageDigest: MessageDigest by lazy {
        MessageDigest.getInstance("SHA-256")
    }

    override fun hash(bytes: ByteArray): ByteArray =
        messageDigest.digest(bytes)
}
