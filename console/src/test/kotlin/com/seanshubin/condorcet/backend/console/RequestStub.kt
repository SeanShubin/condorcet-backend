package com.seanshubin.condorcet.backend.console

import com.seanshubin.condorcet.backend.json.JsonMappers
import com.seanshubin.condorcet.backend.service.http.ServiceCommand
import java.io.BufferedReader
import java.io.StringReader
import java.util.*

class RequestStub(fakeBrowser: FakeBrowser, val target: String, command: ServiceCommand) :
    HttpServletRequestNotImplemented() {
    val requestBody: String
    val stringReader: StringReader
    val bufferedReader: BufferedReader
    val headers: List<Pair<String, String>>

    init {
        requestBody = JsonMappers.pretty.writeValueAsString(command)
        stringReader = StringReader(requestBody)
        bufferedReader = BufferedReader(stringReader)
        headers = headersFromCommand(command) + fakeBrowser.requestHeaders()
    }

    fun toLines(): List<String> {
        val headerLines = headers.map { (name, value) ->
            "$name: $value"
        }
        val bodyLine = requestBody
        return listOf(target) + headerLines + listOf(bodyLine)
    }

    override fun getReader(): BufferedReader {
        return bufferedReader
    }

    override fun getHeaderNames(): Enumeration<String> {
        return Collections.enumeration(headers.map { it.first })
    }

    override fun getHeader(name: String): String =
        headers.withIndex().find { it.value.first == name }!!.value.second

    private fun headersFromCommand(command: ServiceCommand): List<Pair<String, String>> = emptyList()
}
