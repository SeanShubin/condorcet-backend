package com.seanshubin.condorcet.backend.string.util

import kotlin.test.Test
import kotlin.test.assertEquals

class ByteArrayFormatBase64Test {
    @Test
    fun shortCompact() {
        // given
        val bytes = listOf(1, 2, 3, 4, 5, 6).map { it.toByte() }.toByteArray()
        val expected = "AQIDBAUG"
        val byteArrayFormat = ByteArrayFormatBase64()

        // when
        val encoded = byteArrayFormat.encodeCompact(bytes)
        val decoded = byteArrayFormat.decode(encoded)

        // then
        assertEquals(bytes.toList(), decoded.toList())
        assertEquals(expected, encoded)
    }

    @Test
    fun shortPretty() {
        // given
        val bytes = listOf(1, 2, 3, 4, 5, 6).map { it.toByte() }.toByteArray()
        val expected = "AQIDBAUG"
        val byteArrayFormat = ByteArrayFormatBase64()

        // when
        val encoded = byteArrayFormat.encodePretty(bytes)
        val decoded = byteArrayFormat.decode(encoded)

        // then
        assertEquals(bytes.toList(), decoded.toList())
        assertEquals(expected, encoded)
    }

    @Test
    fun longCompact() {
        // given
        val bytes = (0..255).map { it.toByte() }.toByteArray()
        val expected =
            "AAECAwQFBgcICQoLDA0ODxAREhMUFRYXGBkaGxwdHh8gISIjJCUmJygpKissLS4vMDEyMzQ1Njc4OTo7PD0+P0BBQkNERUZHSElKS0xNTk9QUVJTVFVWV1hZWltcXV5fYGFiY2RlZmdoaWprbG1ub3BxcnN0dXZ3eHl6e3x9fn+AgYKDhIWGh4iJiouMjY6PkJGSk5SVlpeYmZqbnJ2en6ChoqOkpaanqKmqq6ytrq+wsbKztLW2t7i5uru8vb6/wMHCw8TFxsfIycrLzM3Oz9DR0tPU1dbX2Nna29zd3t/g4eLj5OXm5+jp6uvs7e7v8PHy8/T19vf4+fr7/P3+/w=="
        val byteArrayFormat = ByteArrayFormatBase64()

        // when
        val encoded = byteArrayFormat.encodeCompact(bytes)
        val decoded = byteArrayFormat.decode(encoded)

        // then
        assertEquals(bytes.toList(), decoded.toList())
        assertEquals(expected, encoded)
    }

    @Test
    fun longPretty() {
        // given
        val bytes = (0..255).map { it.toByte() }.toByteArray()
        val expected = "AAECAwQFBgcICQoLDA0ODxAREhMUFRYXGBkaGxwdHh8gISIjJCUmJygpKissLS4vMDEyMzQ1Njc4" + "\n" +
                "OTo7PD0+P0BBQkNERUZHSElKS0xNTk9QUVJTVFVWV1hZWltcXV5fYGFiY2RlZmdoaWprbG1ub3Bx" + "\n" +
                "cnN0dXZ3eHl6e3x9fn+AgYKDhIWGh4iJiouMjY6PkJGSk5SVlpeYmZqbnJ2en6ChoqOkpaanqKmq" + "\n" +
                "q6ytrq+wsbKztLW2t7i5uru8vb6/wMHCw8TFxsfIycrLzM3Oz9DR0tPU1dbX2Nna29zd3t/g4eLj" + "\n" +
                "5OXm5+jp6uvs7e7v8PHy8/T19vf4+fr7/P3+/w=="
        val byteArrayFormat = ByteArrayFormatBase64()

        // when
        val encoded = byteArrayFormat.encodePretty(bytes)
        val decoded = byteArrayFormat.decode(encoded)

        // then
        assertEquals(bytes.toList(), decoded.toList())
        assertEquals(expected, encoded)
    }

    @Test
    fun padding0() {
        // given
        val bytes = listOf(1, 2, 3).map { it.toByte() }.toByteArray()
        val expected = "AQID"
        val byteArrayFormat = ByteArrayFormatBase64()

        // when
        val encoded = byteArrayFormat.encodeCompact(bytes)
        val decoded = byteArrayFormat.decode(encoded)

        // then
        assertEquals(bytes.toList(), decoded.toList())
        assertEquals(expected, encoded)
    }
    @Test
    fun padding1() {
        // given
        val bytes = listOf(1, 2, 3, 4, 5).map { it.toByte() }.toByteArray()
        val expected = "AQIDBAU="
        val byteArrayFormat = ByteArrayFormatBase64()

        // when
        val encoded = byteArrayFormat.encodeCompact(bytes)
        val decoded = byteArrayFormat.decode(encoded)

        // then
        assertEquals(bytes.toList(), decoded.toList())
        assertEquals(expected, encoded)
    }

    @Test
    fun padding2() {
        // given
        val bytes = listOf(1, 2, 3, 4).map { it.toByte() }.toByteArray()
        val expected = "AQIDBA=="
        val byteArrayFormat = ByteArrayFormatBase64()

        // when
        val encoded = byteArrayFormat.encodeCompact(bytes)
        val decoded = byteArrayFormat.decode(encoded)

        // then
        assertEquals(bytes.toList(), decoded.toList())
        assertEquals(expected, encoded)
    }
}