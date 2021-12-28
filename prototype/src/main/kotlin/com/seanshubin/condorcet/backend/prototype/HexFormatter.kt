package com.seanshubin.condorcet.backend.prototype

interface HexFormatter {
    fun bytesToHex(b: ByteArray): String

    object Compact : HexFormatter {
        override fun bytesToHex(bytes: ByteArray): String {
            return bytes.map(::byteToHex).joinToString("")
        }

        private fun byteToHex(byte: Byte): String {
            val asInt = byte.toInt()
            val digits = "0123456789ABCDEF"
            val lowByte = asInt and 0b1111
            val highByte = asInt shr 4 and 0b1111
            val lowDigit = digits[lowByte]
            val highDigit = digits[highByte]
            return "" + highDigit + lowDigit
        }
    }

    object Pretty : HexFormatter {
        override fun bytesToHex(bytes: ByteArray): String {
            return bytes.map(::byteToHex).windowed(16, 16, partialWindows = true).map { list ->
                list.joinToString(" ")
            }.joinToString("\n")
        }

        private fun byteToHex(byte: Byte): String {
            val asInt = byte.toInt()
            val digits = "0123456789ABCDEF"
            val lowByte = asInt and 0b1111
            val highByte = asInt shr 4 and 0b1111
            val lowDigit = digits[lowByte]
            val highDigit = digits[highByte]
            return "" + highDigit + lowDigit
        }
    }

    companion object {
        val digitValues = mapOf(
            '0' to 0,
            '1' to 1,
            '2' to 2,
            '3' to 3,
            '4' to 4,
            '5' to 5,
            '6' to 6,
            '7' to 7,
            '8' to 8,
            '9' to 9,
            'A' to 10,
            'B' to 11,
            'C' to 12,
            'D' to 13,
            'E' to 14,
            'F' to 15,
        )
        val whitespace = Regex("""\s""")
        fun hexToBytes(s: String): ByteArray {
            return s.replace(whitespace, "").toUpperCase().toList().windowed(2, 2).map { (first, second) ->
                (digitValues.getValue(first) * 16 + digitValues.getValue(second)).toByte()
            }.toByteArray()
        }
    }
}
