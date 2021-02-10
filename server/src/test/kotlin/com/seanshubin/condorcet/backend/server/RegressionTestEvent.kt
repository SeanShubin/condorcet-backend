package com.seanshubin.condorcet.backend.server

import jakarta.servlet.http.Cookie

data class RegressionTestEvent(
    val name: String,
    val method: String,
    val requestBody: String,
    val requestHeaders: List<Pair<String, String>>,
    val requestCookies: List<Cookie>,
    val status: Int,
    val responseBody: String,
    val responseHeaders: List<Pair<String, String>>,
    val responseCookies: List<Cookie>
) {
    fun toLines(): List<String> {
        return listOf(name, method, requestBody) +
                requestHeaders.toLines() +
                requestCookies.toGetCookieLines() +
                listOf(status.toString(), responseBody) +
                responseHeaders.toLines() +
                responseCookies.toSetCookieLines()
    }

    private fun List<Pair<String, String>>.toLines(): List<String> =
        flatMap { it.toLines() }

    private fun Pair<String, String>.toLines(): List<String> =
        listOf("$first -> $second")

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
