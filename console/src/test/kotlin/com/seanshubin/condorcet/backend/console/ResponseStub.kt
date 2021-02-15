package com.seanshubin.condorcet.backend.console

import java.io.PrintWriter
import java.io.StringWriter

class ResponseStub : HttpServletResponseNotImplemented() {
    var status: Int? = null
    val stringWriter = StringWriter()
    val printWriter = PrintWriter(stringWriter)
    val headers: MutableList<Pair<String, String>> = mutableListOf()
    val body: String get() = stringWriter.buffer.toString()
    fun toLines(): List<String> {
        val statusLine = status.toString()
        val headerLines = headers.map { (name, value) ->
            "$name: $value"
        }
        val bodyLine = stringWriter.buffer.toString()
        return listOf(statusLine) + headerLines + listOf(bodyLine)
    }

    override fun setStatus(sc: Int) {
        status = sc
    }

    override fun getWriter(): PrintWriter {
        return printWriter
    }

    override fun addHeader(name: String, value: String) {
        headers.add(Pair(name, value))
    }
}