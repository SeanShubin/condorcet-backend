package com.seanshubin.condorcet.backend.crypto

import com.seanshubin.condorcet.backend.string.util.ByteArrayFormatServiceLocator
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class Sha256HashTest {
    @Test
    fun sameThingsHashSame() {
        // given
        val byteArrayFormat = ByteArrayFormatServiceLocator.byteArrayFormat
        val oneWayHash: OneWayHash =
            Sha256Hash(byteArrayFormat)

        // when
        val hash1 = oneWayHash.hash("same thing")
        val hash2 = oneWayHash.hash("same thing")

        // then
        assertEquals(hash1, hash2)
    }

    @Test
    fun differentThingsHashDifferent() {
        // given
        val byteArrayFormat = ByteArrayFormatServiceLocator.byteArrayFormat
        val oneWayHash: OneWayHash =
            Sha256Hash(byteArrayFormat)

        // when
        val hash1 = oneWayHash.hash("one thing")
        val hash2 = oneWayHash.hash("another thing")

        // then
        assertNotEquals(hash1, hash2)
    }
}
