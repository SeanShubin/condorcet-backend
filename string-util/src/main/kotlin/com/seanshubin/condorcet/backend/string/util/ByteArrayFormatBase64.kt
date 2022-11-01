package com.seanshubin.condorcet.backend.string.util

import java.util.*

class ByteArrayFormatBase64:ByteArrayFormat {
    override fun encodeCompact(bytes: ByteArray): String {
        return Base64.getEncoder().encodeToString(bytes)
    }

    override fun encodePretty(bytes: ByteArray): String {
        val compact = encodeCompact(bytes)
        val lines = compact.chunked(76)
        return lines.joinToString("\n")
    }

    override fun decode(s: String): ByteArray {
        val compact = s.trim().split(whitespace).joinToString("")
        return Base64.getDecoder().decode(compact)
    }
    companion object {
        private val whitespace = Regex("""\s+""")
    }
}
