package com.seanshubin.condorcet.backend.server

import org.eclipse.jetty.server.Handler

class Runner(
    val server: ServerContract,
    val handler: Handler
) : Runnable {
    override fun run() {
        server.setHandler(handler)
        server.start()
        server.join()
    }
}
