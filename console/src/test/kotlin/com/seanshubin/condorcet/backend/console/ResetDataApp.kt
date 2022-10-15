package com.seanshubin.condorcet.backend.console

import com.seanshubin.condorcet.backend.dependencies.Dependencies

object ResetDataApp {
    @JvmStatic
    fun main(args: Array<String>) {
        val dependencies = Dependencies(arrayOf(), ConsoleIntegration())
        val initializer = dependencies.schemaCreator
        initializer.purgeAllData()
        initializer.initialize()
    }
}
