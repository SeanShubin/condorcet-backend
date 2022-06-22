package com.seanshubin.condorcet.backend.server

import com.seanshubin.condorcet.backend.genericdb.SchemaCreator
import com.seanshubin.condorcet.backend.http.Header
import com.seanshubin.condorcet.backend.http.HeaderList
import com.seanshubin.condorcet.backend.http.RequestValue
import com.seanshubin.condorcet.backend.http.ResponseValue
import com.seanshubin.condorcet.backend.io.ioutil.consumeString
import com.seanshubin.condorcet.backend.service.Parsers
import com.seanshubin.condorcet.backend.service.Service
import com.seanshubin.condorcet.backend.service.ServiceException
import com.seanshubin.condorcet.backend.service.http.ServiceCommand
import com.seanshubin.condorcet.backend.service.http.ServiceCommandParser
import com.seanshubin.condorcet.backend.service.http.ServiceEnvironment
import com.seanshubin.condorcet.backend.service.http.TokenUtil
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.handler.AbstractHandler
import java.sql.SQLException

class ApiHandler(
    private val schemaCreator: SchemaCreator,
    private val serviceCommandParser: ServiceCommandParser,
    private val service: Service,
    private val tokenUtil: TokenUtil,
    private val requestEvent: (RequestValue) -> Unit,
    private val responseEvent: (ResponseValue) -> Unit,
    private val topLevelException: (Throwable) -> Unit
) : AbstractHandler() {
    override fun handle(
        target: String,
        baseRequest: Request,
        request: HttpServletRequest,
        response: HttpServletResponse
    ) {
        val serviceRequestName = Parsers.parseCommandNameFromTarget(target)
        val requestValue = request.toRequestValue(target)
        requestEvent(requestValue)
        val serviceCommand = serviceCommandParser.parse(serviceRequestName, requestValue.body)
        val environment = ServiceEnvironment(service, tokenUtil, topLevelException)
        val responseValue = exec(serviceCommand, environment, requestValue)
        responseEvent(responseValue)
        responseValue.writeTo(response)
        baseRequest.isHandled = true
    }

    private fun exec(
        serviceCommand: ServiceCommand,
        environment: ServiceEnvironment,
        requestValue: RequestValue
    ): ResponseValue =
        try {
            serviceCommand.exec(environment, requestValue)
        } catch (ex: ServiceException) {
            ServiceCommand.ServiceExceptionCommand(ex).exec(environment, requestValue)
        } catch (ex: SQLException) {
            if (SchemaCreator.isDatabaseMissing(ex)) {
                schemaCreator.initialize()
                service.synchronize()
                try {
                    serviceCommand.exec(environment, requestValue)
                } catch (ex: ServiceException) {
                    ServiceCommand.ServiceExceptionCommand(ex).exec(environment, requestValue)
                }
            } else {
                ServiceCommand.TopLevelExceptionCommand(ex).exec(environment, requestValue)
            }
        } catch (ex: Throwable) {
            ServiceCommand.TopLevelExceptionCommand(ex).exec(environment, requestValue)
        }

    private fun HttpServletRequest.toRequestValue(target: String): RequestValue {
        val bodyString = reader.consumeString()
        val body = if (bodyString.isBlank()) null else bodyString
        val headerList = headerNames.toList().map { name ->
            val value = getHeader(name)
            Header(name, value)
        }
        val headers = HeaderList(headerList)
        return RequestValue(target, body, headers)
    }

    private fun ResponseValue.writeTo(response: HttpServletResponse) {
        response.status = status
        if (body != null) {
            response.writer.print(body)
        }
        for (header in headers.list) {
            response.addHeader(header.name, header.value)
        }
    }
}
