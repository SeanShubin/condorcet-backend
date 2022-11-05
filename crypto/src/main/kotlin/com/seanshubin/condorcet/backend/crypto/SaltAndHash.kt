package com.seanshubin.condorcet.backend.crypto

import com.seanshubin.condorcet.backend.string.util.ByteArrayFormatServiceLocator
import java.nio.charset.Charset

data class SaltAndHash(val salt: String, val hash: String) {
    companion object {
        private val byteArrayFormat = ByteArrayFormatServiceLocator.byteArrayFormat
        fun fromPassword(
            password: String,
            uniqueIdGenerator: UniqueIdGenerator,
            oneWayHash: OneWayHash,
            charset: Charset
        ): SaltAndHash {
            val saltBytes = uniqueIdGenerator.uniqueId()
            val passwordBytes = password.toByteArray(charset)
            val saltAndPasswordBytes = saltBytes + passwordBytes
            val hashBytes = oneWayHash.hash(saltAndPasswordBytes)
            val salt = byteArrayFormat.encodeCompact(saltBytes)
            val hash = byteArrayFormat.encodeCompact(hashBytes)
            return SaltAndHash(salt, hash)
        }

        fun validate(
            password: String,
            saltAndHash: SaltAndHash,
            oneWayHash: OneWayHash,
            charset: Charset
        ): Boolean {
            val (salt, expectedHash) = saltAndHash
            val saltBytes = byteArrayFormat.decode(salt)
            val passwordBytes = password.toByteArray(charset)
            val actualHashBytes = oneWayHash.hash(saltBytes + passwordBytes)
            val actualHash = byteArrayFormat.encodeCompact(actualHashBytes)
            return actualHash == expectedHash
        }
    }
}
