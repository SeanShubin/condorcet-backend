package com.seanshubin.condorcet.backend.server

import com.seanshubin.condorcet.backend.domain.Parsers
import com.seanshubin.condorcet.backend.domain.Service
import com.seanshubin.condorcet.backend.domain.ServiceEventParser
import com.seanshubin.condorcet.backend.domain.ServiceResponse
import com.seanshubin.condorcet.backend.io.ioutil.consumeString
import com.seanshubin.condorcet.backend.json.JsonMappers
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.handler.AbstractHandler

class ApiHandler(
    private val serviceEventParser: ServiceEventParser,
    private val service: Service
) : AbstractHandler() {
    override fun handle(
        target: String,
        baseRequest: Request,
        request: HttpServletRequest,
        response: HttpServletResponse
    ) {
        val serviceEventName = Parsers.parseCommandNameFromTarget(target)
        val requestBody = request.reader.consumeString()
        val serviceEvent = serviceEventParser.parse(serviceEventName, requestBody)
        val result = serviceEvent.exec(service)
        val responseBody = JsonMappers.pretty.writeValueAsString(result)
        response.contentType = "application/json"
        response.writer.print(responseBody)

        response.status = statusCodeMap[result::class] ?: 200
        baseRequest.isHandled = true
    }

    private val statusCodeMap = mapOf(
        ServiceResponse.Unauthorized::class to 401,
        ServiceResponse.NotFound::class to 404,
        ServiceResponse.Conflict::class to 409,
        ServiceResponse.Unsupported::class to 400,
        ServiceResponse.MalformedJson::class to 400
    )
}
