package com.seanshubin.condorcet.backend.server

data class Event(
    val name: String,
    val method: String,
    val requestBody: String,
    val status: Int,
    val responseBody: String
) {
    fun toLines(): List<String> {
        return listOf(name, method, requestBody, status.toString(), responseBody)
    }

    companion object {
        fun consumeFromLines(lines: List<String>): Pair<List<String>, Event> {
            var index = 0
            val name = lines[index++]
            val method = lines[index++]
            if (lines[index] != "{") throw RuntimeException("'{' expected")
            val requestBodyLines = mutableListOf<String>()
            while (lines[index] != "}") {
                requestBodyLines.add(lines[index++])
            }
            requestBodyLines.add(lines[index++])
            val requestBody = requestBodyLines.joinToString("\n")
            val status = lines[index++].toInt()
            val responseBodyLines = mutableListOf<String>()
            if (lines[index] != "{") throw RuntimeException("'{' expected")
            while (lines[index] != "}") {
                responseBodyLines.add(lines[index++])
            }
            responseBodyLines.add(lines[index++])
            val responseBody = responseBodyLines.joinToString("\n")
            val remainingLines = lines.drop(index)
            val event = Event(name, method, requestBody, status, responseBody)
            return Pair(remainingLines, event)
        }
    }
}
