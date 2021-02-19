package com.seanshubin.condorcet.backend.console

import com.seanshubin.condorcet.backend.dependencies.Dependencies
import com.seanshubin.condorcet.backend.domain.Role

object SampleDataApp {
    @JvmStatic
    fun main(args: Array<String>) {
        val dependencies = Dependencies(ConsoleIntegration())
        val initializer = dependencies.initializer
        val service = dependencies.service
        initializer.purgeAllData()
        initializer.initialize()
        val aliceAccessToken = service.register("Alice", "alice@email.com", "pass").accessToken
        service.register("Bob", "bob@email.com", "pass")
        service.register("Carol", "carol@email.com", "pass")
        service.register("Dave", "dave@email.com", "pass")
        service.setRole(aliceAccessToken, "Bob", Role.ADMIN)
        service.setRole(aliceAccessToken, "Carol", Role.USER)
    }
}