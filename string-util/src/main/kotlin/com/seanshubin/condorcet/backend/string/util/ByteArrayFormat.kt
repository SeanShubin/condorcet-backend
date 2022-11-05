package com.seanshubin.condorcet.backend.string.util

interface ByteArrayFormat {
    fun encodeCompact(bytes: ByteArray): String
    fun encodePretty(bytes: ByteArray): String
    fun decode(s: String): ByteArray
}