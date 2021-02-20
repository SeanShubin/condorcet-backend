package com.seanshubin.condorcet.backend.server

import com.seanshubin.condorcet.backend.genericdb.SchemaCreator
import com.seanshubin.condorcet.backend.service.Service
import org.eclipse.jetty.server.Handler

class ServerRunner(
    private val schemaCreator: SchemaCreator,
    private val server: ServerContract,
    private val handler: Handler,
    private val service: Service
) : Runnable {
    override fun run() {
        schemaCreator.initialize()
        service.synchronize()
        service
        server.setHandler(handler)
        server.start()
        server.join()
    }
}
