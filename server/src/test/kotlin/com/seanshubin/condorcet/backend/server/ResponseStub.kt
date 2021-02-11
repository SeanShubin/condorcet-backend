package com.seanshubin.condorcet.backend.server

import java.io.PrintWriter
import java.io.StringWriter

class ResponseStub : HttpServletResponseNotImplemented() {
    var theContentType: String? = null
    var theStatus: Int? = null
    val stringWriter = StringWriter()
    val printWriter = PrintWriter(stringWriter)
    val headers: MutableList<Pair<String, String>> = mutableListOf()

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

    override fun addHeader(name: String, value: String) {
        headers.add(Pair(name, value))
    }

    override fun getHeader(name: String): String =
        headers.find { it.first.equals(name, ignoreCase = true) }?.second!!
}
