package com.seanshubin.condorcet.backend.console

import com.seanshubin.condorcet.backend.crypto.*
import com.seanshubin.condorcet.backend.domain.ApiParser
import com.seanshubin.condorcet.backend.domain.ApiService
import com.seanshubin.condorcet.backend.domain.Parser
import com.seanshubin.condorcet.backend.domain.Service
import com.seanshubin.condorcet.backend.server.ApiHandler
import com.seanshubin.condorcet.backend.server.JettyServer
import com.seanshubin.condorcet.backend.server.Runner
import com.seanshubin.condorcet.backend.server.ServerContract
import org.eclipse.jetty.server.Handler
import org.eclipse.jetty.server.Server

class Dependencies {
    val port: Int = 8080
    val server: Server = Server(port)
    val serverContract: ServerContract = JettyServer(server)
    val parser: Parser = ApiParser()
    val uniqueIdGennerator: UniqueIdGenerator = Uuid4()
    val oneWayHash: OneWayHash = Sha256Hash()
    val passwordUtil: PasswordUtil = PasswordUtil(uniqueIdGennerator, oneWayHash)
    val service: Service = ApiService(passwordUtil)
    val handler: Handler = ApiHandler(parser, service)
    val runner: Runnable = Runner(serverContract, handler)
}
