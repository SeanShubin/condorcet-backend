package com.seanshubin.condorcet.backend.http

data class RequestValue(
    val target: String,
    val body: String,
    val headers: HeaderList
) {
    fun headerValue(name: String): String? = headers.headerValue(name)
    fun cookies(): CookieList = headers.cookies()
    fun cookieValue(name: String): String? = headers.cookieValue(name)
    fun bearerToken(): String? = headers.bearerToken()
    fun toLines(): List<String> = listOf(target, body) + headers.toLines()
}
