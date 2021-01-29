package com.seanshubin.condorcet.backend.server

import com.seanshubin.condorcet.backend.domain.*
import com.seanshubin.condorcet.backend.io.ioutil.consumeString
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.handler.AbstractHandler

class ApiHandler(
    private val parser: Parser,
    private val service: Service
) : AbstractHandler() {
    override fun handle(
        target: String,
        baseRequest: Request,
        request: HttpServletRequest,
        response: HttpServletResponse
    ) {
        val commandName = Parsers.parseCommandNameFromTarget(target)
        val requestBody = request.reader.consumeString()
        val command = parser.parse(commandName, requestBody)
        val result = command.exec(service)
        val responseBody = JsonMappers.pretty.writeValueAsString(result)
        response.contentType = "application/json"
        response.writer.print(responseBody)

        response.status = statusCodeMap[result::class] ?: 200
        baseRequest.isHandled = true
    }

    private val statusCodeMap = mapOf(
        Response.Unauthorized::class to 401,
        Response.NotFound::class to 404,
        Response.Conflict::class to 409,
        Response.Unsupported::class to 400
    )
}
