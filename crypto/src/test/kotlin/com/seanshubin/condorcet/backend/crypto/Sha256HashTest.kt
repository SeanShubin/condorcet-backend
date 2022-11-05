package com.seanshubin.condorcet.backend.crypto

import com.seanshubin.condorcet.backend.string.util.ByteArrayFormatServiceLocator
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class Sha256HashTest {
    val byteArrayFormat = ByteArrayFormatServiceLocator.byteArrayFormat

    @Test
    fun sameThingsHashSame() {
        // given
        val oneWayHash: OneWayHash = Sha256Hash

        // when
        val hash1 = byteArrayFormat.encodeCompact(oneWayHash.hash("same thing".toByteArray()))
        val hash2 = byteArrayFormat.encodeCompact(oneWayHash.hash("same thing".toByteArray()))

        // then
        assertEquals(hash1, hash2)
    }

    @Test
    fun differentThingsHashDifferent() {
        // given
        val oneWayHash: OneWayHash = Sha256Hash

        // when
        val hash1 = byteArrayFormat.encodeCompact(oneWayHash.hash("one thing".toByteArray()))
        val hash2 = byteArrayFormat.encodeCompact(oneWayHash.hash("another thing".toByteArray()))

        // then
        assertNotEquals(hash1, hash2)
    }
}
