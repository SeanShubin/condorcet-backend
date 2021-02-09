package com.seanshubin.condorcet.backend.server

import com.seanshubin.condorcet.backend.io.ioutil.consumeString
import com.seanshubin.condorcet.backend.json.JsonMappers
import com.seanshubin.condorcet.backend.service.*
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.handler.AbstractHandler

class ApiHandler(
    private val serviceRequestParser: ServiceRequestParser,
    private val service: Service,
    private val requestEvent: (String, String) -> Unit,
    private val responseEvent: (Int, String) -> Unit
) : AbstractHandler() {
    override fun handle(
        target: String,
        baseRequest: Request,
        request: HttpServletRequest,
        response: HttpServletResponse
    ) {
        val serviceRequestName = Parsers.parseCommandNameFromTarget(target)
        val requestBody = request.reader.consumeString()
        requestEvent(target, requestBody)
        val serviceRequest = serviceRequestParser.parse(serviceRequestName, requestBody)
        val serviceResponse = exec(serviceRequest)
        val responseBody = JsonMappers.pretty.writeValueAsString(serviceResponse.value)
        responseEvent(serviceResponse.status, responseBody)
        response.contentType = "application/json"
        response.writer.print(responseBody)
        response.status = serviceResponse.status
        baseRequest.isHandled = true
    }

    private fun exec(serviceRequest: ServiceRequest): ServiceResponse = try {
        ServiceResponse(200, serviceRequest.exec(service))
    } catch (ex: ServiceException) {
        val statusCode = statusCodeMap[ex::class] ?: 500
        ServiceResponse(statusCode, mapOf("userSafeMessage" to ex.userSafeMessage))
    }

    private val statusCodeMap = mapOf(
        ServiceException.Unauthorized::class to 401,
        ServiceException.NotFound::class to 404,
        ServiceException.Conflict::class to 409,
        ServiceException.Unsupported::class to 400,
        ServiceException.MalformedJson::class to 400
    )
}
