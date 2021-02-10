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
    private val serviceEnvironmentFactory: ServiceEnvironmentFactory,
    private val tokenService: TokenService,
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
        val accessToken = tokenService.getAccessToken(request)
        val refreshToken = tokenService.getRefreshToken(request)
        val serviceEnvironment = serviceEnvironmentFactory.createEnvironment(accessToken, refreshToken)
        val serviceRequest = serviceRequestParser.parse(serviceRequestName, requestBody)
        val httpResponse = exec(serviceEnvironment, serviceRequest)
        val responseBody = JsonMappers.pretty.writeValueAsString(httpResponse.value)
        responseEvent(httpResponse.status, responseBody)
        response.contentType = "application/json"
        response.writer.print(responseBody)
        response.status = httpResponse.status
        tokenService.setRefreshToken(response, httpResponse.refreshToken)
        baseRequest.isHandled = true
    }

    private fun exec(
        serviceEnvironment: ServiceEnvironment,
        serviceRequest: ServiceRequest
    ): HttpResponse = try {
        val serviceResponse = serviceRequest.exec(serviceEnvironment)
        val httpResponse = serviceResponse.toHttpResponse(200)
        httpResponse
    } catch (ex: ServiceException) {
        val statusCode = statusCodeMap[ex::class] ?: 500
        HttpResponse(
            status = statusCode,
            value = mapOf("userSafeMessage" to ex.userSafeMessage),
            refreshToken = null
        )
    }

    private val statusCodeMap = mapOf(
        ServiceException.Unauthorized::class to 401,
        ServiceException.NotFound::class to 404,
        ServiceException.Conflict::class to 409,
        ServiceException.Unsupported::class to 400,
        ServiceException.MalformedJson::class to 400
    )

    private fun ServiceResponse.toHttpResponse(status: Int) = HttpResponse(
        status = status,
        value = value,
        refreshToken = refreshToken
    )
}
