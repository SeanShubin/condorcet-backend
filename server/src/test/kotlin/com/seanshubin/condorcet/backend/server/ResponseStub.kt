package com.seanshubin.condorcet.backend.server

import jakarta.servlet.http.Cookie
import java.io.PrintWriter
import java.io.StringWriter

class ResponseStub(
    private val cookieSimulator: CookieSimulator,
    private val headers: List<Pair<String, String>> = emptyList()
) : HttpServletResponseNotImplemented() {
    var theContentType: String? = null
    var theStatus: Int? = null
    val stringWriter = StringWriter()
    val printWriter = PrintWriter(stringWriter)

    override fun getHeaderNames(): MutableCollection<String> =
        headers.map { it.first }.toMutableList()

    override fun setContentType(type: String?) {
        theContentType = type
    }

    override fun getStatus(): Int {
        return theStatus!!
    }

    override fun setStatus(sc: Int) {
        theStatus = sc
    }

    override fun getWriter(): PrintWriter {
        return printWriter
    }

    override fun addCookie(cookie: Cookie) {
        cookieSimulator.addCookie(cookie)
    }
}
