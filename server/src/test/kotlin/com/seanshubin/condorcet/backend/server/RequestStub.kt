package com.seanshubin.condorcet.backend.server

import java.io.BufferedReader
import java.io.StringReader
import java.util.*

class RequestStub(
    body: String?,
    private val headers: List<Pair<String, String>> = emptyList()
) : HttpServletRequestNotImplemented() {
    private val stringReader = StringReader(body ?: "")
    private val theReader = BufferedReader(stringReader)
    override fun getReader(): BufferedReader {
        return theReader
    }

    override fun getHeaderNames(): Enumeration<String> =
        Collections.enumeration(headers.map { it.first })

    override fun getHeader(name: String?): String? {
        return headers.find { it.first == name }?.second
    }
}
