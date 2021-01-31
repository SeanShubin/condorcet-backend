package com.seanshubin.condorcet.backend.console

object ServerApp {
    @JvmStatic
    fun main(args: Array<String>) {
        Dependencies().runner.run()
    }
}
