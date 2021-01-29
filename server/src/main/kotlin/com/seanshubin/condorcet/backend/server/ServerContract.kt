package com.seanshubin.condorcet.backend.server

import org.eclipse.jetty.server.Handler

interface ServerContract {
    fun start()
    fun join()
    fun setHandler(handler: Handler)
}
