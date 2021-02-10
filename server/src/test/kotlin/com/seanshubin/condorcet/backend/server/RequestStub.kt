package com.seanshubin.condorcet.backend.server

import jakarta.servlet.http.Cookie
import java.io.BufferedReader
import java.io.StringReader
import java.util.*

class RequestStub(
    private val name: String,
    private val theMethod: String,
    private val body: String,
    private val cookieSimulator: CookieSimulator,
    private val headers: List<Pair<String, String>> = emptyList()
) : HttpServletRequestNotImplemented() {
    private val stringReader = StringReader(body)
    private val theReader = BufferedReader(stringReader)
    override fun getMethod(): String = theMethod
    override fun getReader(): BufferedReader {
        return theReader
    }

    override fun getHeaderNames(): Enumeration<String> =
        Collections.enumeration(headers.map { it.first })

    override fun getHeader(name: String?): String? {
        return headers.find { it.first == name }?.second
    }

    override fun getCookies(): Array<Cookie>? {
        return cookieSimulator.getCookies()
    }
}
