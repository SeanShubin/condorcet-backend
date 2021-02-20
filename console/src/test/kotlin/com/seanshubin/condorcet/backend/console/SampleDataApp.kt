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
        service.register("Eve", "eve@email.com", "pass").accessToken
        service.register("Frank", "frank@email.com", "pass").accessToken
        service.register("Grace", "grace@email.com", "pass")
        service.register("Heidi", "heidi@email.com", "pass")
        service.register("Ivy", "ivy@email.com", "pass").accessToken
        service.register("Judy", "judy@email.com", "pass").accessToken
        service.setRole(aliceAccessToken, "Bob", Role.AUDITOR)
        service.setRole(aliceAccessToken, "Carol", Role.ADMIN)
        val carolAccessToken = service.authenticate("Carol", "pass").accessToken
        service.setRole(aliceAccessToken, "Dave", Role.ADMIN)
        val daveAccessToken = service.authenticate("Dave", "pass").accessToken
        service.setRole(carolAccessToken, "Eve", Role.USER)
        val eveAccessToken = service.authenticate("Eve", "pass").accessToken
        service.setRole(carolAccessToken, "Frank", Role.USER)
        val frankAccessToken = service.authenticate("Frank", "pass").accessToken
        service.setRole(carolAccessToken, "Grace", Role.USER)
        service.setRole(daveAccessToken, "Heidi", Role.USER)
        service.setRole(daveAccessToken, "Ivy", Role.USER)
        val ivyAccessToken = service.authenticate("Ivy", "pass").accessToken
        service.setRole(daveAccessToken, "Judy", Role.USER)
        val judyAccessToken = service.authenticate("Judy", "pass").accessToken
        service.addElection(eveAccessToken, "Favorite Ice Cream Flavor")
        service.addElection(frankAccessToken, "Government")
        service.addElection(frankAccessToken, "Dystopia")
        service.addElection(ivyAccessToken, "Pet")
        service.addElection(judyAccessToken, "Fantasy")
        service.addElection(judyAccessToken, "Science Fiction")
    }
}