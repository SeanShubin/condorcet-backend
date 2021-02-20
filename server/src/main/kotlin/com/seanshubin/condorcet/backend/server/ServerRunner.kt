package com.seanshubin.condorcet.backend.server

import org.eclipse.jetty.server.Handler

class ServerRunner(
    private val server: ServerContract,
    private val handler: Handler
) : Runnable {
    override fun run() {
        server.setHandler(handler)
        server.start()
        server.join()
    }
}
