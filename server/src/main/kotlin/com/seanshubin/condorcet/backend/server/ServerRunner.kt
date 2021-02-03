package com.seanshubin.condorcet.backend.server

import com.seanshubin.condorcet.backend.genericdb.Initializer
import com.seanshubin.condorcet.backend.service.Lifecycles
import org.eclipse.jetty.server.Handler

class ServerRunner(
    private val lifecycles: Lifecycles,
    private val initializer: Initializer,
    private val server: ServerContract,
    private val handler: Handler
) : Runnable {
    override fun run() {
        lifecycles.doInLifecycle {
            initializer.initialize()
            server.setHandler(handler)
            server.start()
            server.join()
        }
    }
}
