package com.seanshubin.condorcet.backend.console

import com.seanshubin.condorcet.backend.dependencies.DeterministicDependencies

object ServerApp {
    @JvmStatic
    fun main(args: Array<String>) {
        DeterministicDependencies(ConsoleIntegration()).runner.run()
    }
}
