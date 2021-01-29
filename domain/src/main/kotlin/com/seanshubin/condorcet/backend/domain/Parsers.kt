package com.seanshubin.condorcet.backend.domain

object Parsers {
    private val parseCommandNameFromTargetRegex = Regex("""/([^/]+).*""")
    fun parseCommandNameFromTarget(target: String): String {
        val matchResult = parseCommandNameFromTargetRegex.matchEntire(target)
        if (matchResult == null) {
            throw RuntimeException("Value '$target' did not match pattern $parseCommandNameFromTargetRegex")
        } else {
            return matchResult.groupValues[1]
        }
    }
}
