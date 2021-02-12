package com.seanshubin.condorcet.backend.server

import jakarta.servlet.http.Cookie

data class RegressionTestEvent(
    val name: String,
    val method: String,
    val requestBody: String?,
    val requestHeaders: List<Pair<String, String>>,
    val status: Int,
    val responseBody: String?,
    val responseHeaders: List<Pair<String, String>>
) {
    fun toLines(): List<String> {
        val maybeRequestBody = if (requestBody == null) emptyList() else listOf(requestBody)
        val maybeResponseBody = if (responseBody == null) emptyList() else listOf(responseBody)
        return listOf(name, method) +
                maybeRequestBody +
                requestHeaders.toLines() +
                listOf(status.toString()) +
                maybeResponseBody +
                responseHeaders.toLines()
    }

    private fun List<Pair<String, String>>.toLines(): List<String> =
        flatMap { it.toLines() }

    private fun Pair<String, String>.toLines(): List<String> =
        listOf("$first: $second")

    private fun List<Cookie>.toGetCookieLines(): List<String> =
        if (isEmpty()) emptyList()
        else {
            val parts = this.map { it.toGetCookieElement() }
            val asString = parts.joinToString("; ")
            val line = "Cookie: $asString"
            listOf(line)
        }

    private fun List<Cookie>.toSetCookieLines(): List<String> =
        map { it.toSetCookieLine() }

    private fun Cookie.toSetCookieLine(): String =
        "Set-Cookie: ${this.toGetCookieElement()}"

    private fun Cookie.toGetCookieElement(): String =
        "${name}=${value}"

}
