package com.seanshubin.condorcet.backend.server

import org.eclipse.jetty.server.Handler
import org.eclipse.jetty.server.Server

class JettyServer(private val server: Server) : ServerContract {
    override fun setHandler(handler: Handler) {
        server.handler = handler
    }

    override fun start() {
        server.start()
    }

    override fun join() {
        server.join()
    }
}
