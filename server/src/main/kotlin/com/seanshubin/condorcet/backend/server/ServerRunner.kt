package com.seanshubin.condorcet.backend.server

import com.seanshubin.condorcet.backend.genericdb.Initializer
import org.eclipse.jetty.server.Handler

class ServerRunner(
    private val initializer: Initializer,
    private val server: ServerContract,
    private val handler: Handler
) : Runnable {
    override fun run() {
        initializer.initialize()
        server.setHandler(handler)
        server.start()
        server.join()
    }
}
