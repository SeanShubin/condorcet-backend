package com.seanshubin.condorcet.backend.console

import com.seanshubin.condorcet.backend.dependencies.Dependencies

object ServerApp {
    @JvmStatic
    fun main(args: Array<String>) {
        Dependencies(args, ConsoleIntegration()).runner.run()
    }
}
