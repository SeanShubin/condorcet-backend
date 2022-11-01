package com.seanshubin.condorcet.backend.string.util

import kotlin.test.Test
import kotlin.test.assertEquals

class ByteArrayFormatHexTest {
    @Test
    fun shortCompact() {
        // given
        val bytes = listOf(1, 40, 99, 127, 128, 255).map { it.toByte() }.toByteArray()
        val expected = "0128637F80FF"
        val byteArrayFormat = ByteArrayFormatHex()

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
        val bytes = listOf(1, 40, 99, 127, 128, 255).map { it.toByte() }.toByteArray()
        val expected = "01 28 63 7F 80 FF"
        val byteArrayFormat = ByteArrayFormatHex()

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
        val expected = "000102030405060708090A0B0C0D0E0F" +
                "101112131415161718191A1B1C1D1E1F" +
                "202122232425262728292A2B2C2D2E2F" +
                "303132333435363738393A3B3C3D3E3F" +
                "404142434445464748494A4B4C4D4E4F" +
                "505152535455565758595A5B5C5D5E5F" +
                "606162636465666768696A6B6C6D6E6F" +
                "707172737475767778797A7B7C7D7E7F" +
                "808182838485868788898A8B8C8D8E8F" +
                "909192939495969798999A9B9C9D9E9F" +
                "A0A1A2A3A4A5A6A7A8A9AAABACADAEAF" +
                "B0B1B2B3B4B5B6B7B8B9BABBBCBDBEBF" +
                "C0C1C2C3C4C5C6C7C8C9CACBCCCDCECF" +
                "D0D1D2D3D4D5D6D7D8D9DADBDCDDDEDF" +
                "E0E1E2E3E4E5E6E7E8E9EAEBECEDEEEF" +
                "F0F1F2F3F4F5F6F7F8F9FAFBFCFDFEFF"
        val byteArrayFormat = ByteArrayFormatHex()

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
        val byteArrayFormat = ByteArrayFormatHex()
        val expected = "00 01 02 03 04 05 06 07 08 09 0A 0B 0C 0D 0E 0F\n" +
                "10 11 12 13 14 15 16 17 18 19 1A 1B 1C 1D 1E 1F\n" +
                "20 21 22 23 24 25 26 27 28 29 2A 2B 2C 2D 2E 2F\n" +
                "30 31 32 33 34 35 36 37 38 39 3A 3B 3C 3D 3E 3F\n" +
                "40 41 42 43 44 45 46 47 48 49 4A 4B 4C 4D 4E 4F\n" +
                "50 51 52 53 54 55 56 57 58 59 5A 5B 5C 5D 5E 5F\n" +
                "60 61 62 63 64 65 66 67 68 69 6A 6B 6C 6D 6E 6F\n" +
                "70 71 72 73 74 75 76 77 78 79 7A 7B 7C 7D 7E 7F\n" +
                "80 81 82 83 84 85 86 87 88 89 8A 8B 8C 8D 8E 8F\n" +
                "90 91 92 93 94 95 96 97 98 99 9A 9B 9C 9D 9E 9F\n" +
                "A0 A1 A2 A3 A4 A5 A6 A7 A8 A9 AA AB AC AD AE AF\n" +
                "B0 B1 B2 B3 B4 B5 B6 B7 B8 B9 BA BB BC BD BE BF\n" +
                "C0 C1 C2 C3 C4 C5 C6 C7 C8 C9 CA CB CC CD CE CF\n" +
                "D0 D1 D2 D3 D4 D5 D6 D7 D8 D9 DA DB DC DD DE DF\n" +
                "E0 E1 E2 E3 E4 E5 E6 E7 E8 E9 EA EB EC ED EE EF\n" +
                "F0 F1 F2 F3 F4 F5 F6 F7 F8 F9 FA FB FC FD FE FF"

        // when
        val encoded = byteArrayFormat.encodePretty(bytes)
        val decoded = byteArrayFormat.decode(encoded)

        // then
        assertEquals(bytes.toList(), decoded.toList())
        assertEquals(expected, encoded)
    }
}