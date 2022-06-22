package com.seanshubin.condorcet.backend.console

import com.seanshubin.condorcet.backend.dependencies.Dependencies
import com.seanshubin.condorcet.backend.service.http.ServiceCommand
import org.eclipse.jetty.server.Request

class RegressionTestRunner(
    private val dependencies: Dependencies,
    private val commands: List<ServiceCommand>
) {
    fun run() {
        dependencies.schemaCreator.purgeAllData()
        dependencies.schemaCreator.initialize()
        val fakeBrowser = FakeBrowser()
        commands.forEachIndexed { index, command ->
            handleCommand(fakeBrowser, index, command)
        }
        dependencies.schemaCreator.listAllData()
        dependencies.schemaCreator.listAllDebugData()
    }

    fun handleCommand(fakeBrowser: FakeBrowser, index: Int, command: ServiceCommand) {
        val target = "/" + command.javaClass.simpleName
        val baseRequest = Request(null, null)
        val request = RequestStub(fakeBrowser, target, command)
        val response = ResponseStub()
        dependencies.handler.handle(target, baseRequest, request, response)
        if (response.status != 200) {
            val header =
                "command[$index] failed\nRegression test is for happy path only, only responses with status 200 expected"
            val lines = listOf(header) + request.toLines() + listOf("") + response.toLines()
            throw RuntimeException(lines.joinToString("\n"))
        }
        fakeBrowser.handleResponse(response)
    }

}
