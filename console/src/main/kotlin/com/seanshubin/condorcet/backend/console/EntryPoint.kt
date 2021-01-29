package com.seanshubin.condorcet.backend.console

object EntryPoint {
    @JvmStatic
    fun main(args: Array<String>) {
        Dependencies().runner.run()
    }
}
