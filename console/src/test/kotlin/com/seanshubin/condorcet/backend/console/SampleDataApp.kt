package com.seanshubin.condorcet.backend.console

import com.seanshubin.condorcet.backend.dependencies.Dependencies
import com.seanshubin.condorcet.backend.domain.ElectionUpdates
import com.seanshubin.condorcet.backend.domain.Ranking
import com.seanshubin.condorcet.backend.domain.Role.*
import com.seanshubin.condorcet.backend.service.AccessToken
import com.seanshubin.condorcet.backend.service.Service

object SampleDataApp {
    val alice = AccessToken("Alice", OWNER)
    val bob = AccessToken("Bob", AUDITOR)
    val carol = AccessToken("Carol", AUDITOR)
    val dave = AccessToken("Dave", ADMIN)
    val eve = AccessToken("Eve", ADMIN)
    val frank = AccessToken("Frank", ADMIN)
    val grace = AccessToken("Grace", USER)
    val heidi = AccessToken("Heidi", USER)
    val ivy = AccessToken("Ivy", USER)
    val judy = AccessToken("Judy", USER)

    @JvmStatic
    fun main(args: Array<String>) {
        val dependencies = Dependencies(arrayOf(), ConsoleIntegration())
        val initializer = dependencies.schemaCreator
        val service = dependencies.service
        initializer.purgeAllData()
        initializer.initialize()
        service.register("Alice", "alice@email.com", "pass")
        service.register("Bob", "bob@email.com", "pass")
        service.register("Carol", "carol@email.com", "pass")
        service.register("Dave", "dave@email.com", "pass")
        service.register("Eve", "eve@email.com", "pass")
        service.register("Frank", "frank@email.com", "pass")
        service.register("Grace", "grace@email.com", "pass")
        service.register("Heidi", "heidi@email.com", "pass")
        service.register("Ivy", "ivy@email.com", "pass")
        service.register("Judy", "judy@email.com", "pass")
        service.register("Mallory", "mallory@email.com", "pass")
        service.register("Trent", "trent@email.com", "pass")
        service.register("Walter", "walter@email.com", "pass")
        service.register("Peggy", "peggy@email.com", "pass")
        service.register("Victor", "victor@email.com", "pass")
        service.setRole(alice, "Bob", AUDITOR)
        service.setRole(alice, "Carol", AUDITOR)
        service.setRole(alice, "Dave", ADMIN)
        service.setRole(alice, "Eve", ADMIN)
        service.setRole(alice, "Frank", ADMIN)
        service.setRole(alice, "Grace", USER)
        service.setRole(alice, "Heidi", USER)
        service.setRole(alice, "Ivy", USER)
        service.setRole(alice, "Judy", USER)

        createSpoilerAlertElection(service)
        createOpeningMoveElection(service)
        createCredibleCompromiseElection(service)
        createPetsElection(service)
    }

    private fun createSpoilerAlertElection(service: Service) {
        val electionName = "Spoiler Alert"
        service.addElection(grace, electionName)
        service.updateElection(grace, electionName, ElectionUpdates(secretBallot = true))
        service.setCandidates(grace, electionName, listOf("Minor Improvements", "Status Quo", "Radical Changes"))
        service.launchElection(grace, electionName, false)
        service.castBallot(
            alice,
            "Alice",
            electionName,
            listOf(Ranking("Status Quo", 2), Ranking("Radical Changes", null), Ranking("Minor Improvements", 1))
        )
        service.castBallot(
            bob,
            "Bob",
            electionName,
            listOf(Ranking("Minor Improvements", 1), Ranking("Status Quo", 2), Ranking("Radical Changes", 3))
        )
        service.castBallot(
            carol,
            "Carol",
            electionName,
            listOf(Ranking("Radical Changes", 4), Ranking("Minor Improvements", 1), Ranking("Status Quo", 2))
        )
        service.castBallot(
            dave,
            "Dave",
            electionName,
            listOf(Ranking("Minor Improvements", 2), Ranking("Radical Changes", null), Ranking("Status Quo", 1))
        )
        service.castBallot(
            eve,
            "Eve",
            electionName,
            listOf(Ranking("Status Quo", 1), Ranking("Radical Changes", 3), Ranking("Minor Improvements", 2))
        )
        service.castBallot(
            frank,
            "Frank",
            electionName,
            listOf(Ranking("Radical Changes", 3), Ranking("Minor Improvements", 2), Ranking("Status Quo", 1))
        )
        service.castBallot(
            grace,
            "Grace",
            electionName,
            listOf(Ranking("Radical Changes", 1), Ranking("Minor Improvements", 2), Ranking("Status Quo", 3))
        )
        service.castBallot(
            heidi,
            "Heidi",
            electionName,
            listOf(Ranking("Status Quo", null), Ranking("Radical Changes", 1), Ranking("Minor Improvements", 2))
        )
        service.castBallot(
            ivy,
            "Ivy",
            electionName,
            listOf(Ranking("Minor Improvements", 20), Ranking("Radical Changes", 10), Ranking("Status Quo", 30))
        )
        service.castBallot(
            judy,
            "Judy",
            electionName,
            listOf(Ranking("Minor Improvements", 2), Ranking("Status Quo", 3), Ranking("Radical Changes", 1))
        )
        service.finalizeElection(grace, electionName)
    }

    private fun createOpeningMoveElection(service: Service) {
        val electionName = "Priorities for Savers, Speculators, and Restaurant Owners"
        service.addElection(heidi, electionName)
        service.setCandidates(
            heidi,
            electionName,
            listOf("Cheaper Food", "Less Regulations", "Lower Taxes")
        )
        service.launchElection(heidi, electionName, allowEdit = false)
        val saverPreference = listOf(
            Ranking("Cheaper Food", 1),
            Ranking("Lower Taxes", 2),
            Ranking("Less Regulations", 3)
        )
        val restaurantOwnerPreferences = listOf(
            Ranking("Less Regulations", 1),
            Ranking("Cheaper Food", 2),
            Ranking("Lower Taxes", 3)
        )
        val speculatorPreferences = listOf(
            Ranking("Lower Taxes", 1),
            Ranking("Less Regulations", 2),
            Ranking("Cheaper Food", 3)
        )
        service.castBallot(
            alice,
            "Alice",
            electionName,
            saverPreference
        )
        service.castBallot(
            bob,
            "Bob",
            electionName,
            saverPreference
        )
        service.castBallot(
            carol,
            "Carol",
            electionName,
            saverPreference
        )
        service.castBallot(
            dave,
            "Dave",
            electionName,
            restaurantOwnerPreferences
        )
        service.castBallot(
            eve,
            "Eve",
            electionName,
            restaurantOwnerPreferences
        )
        service.castBallot(
            frank,
            "Frank",
            electionName,
            restaurantOwnerPreferences
        )
        service.castBallot(
            grace,
            "Grace",
            electionName,
            speculatorPreferences
        )
        service.castBallot(
            heidi,
            "Heidi",
            electionName,
            speculatorPreferences
        )
        service.castBallot(
            ivy,
            "Ivy",
            electionName,
            speculatorPreferences
        )
        service.castBallot(
            judy,
            "Judy",
            electionName,
            saverPreference
        )
        service.finalizeElection(heidi, electionName)
    }


    private fun createCredibleCompromiseElection(service: Service) {
        val electionName = "Credible Compromise"
        service.addElection(ivy, electionName)
        service.setCandidates(
            ivy,
            electionName,
            listOf("Relatively Unknown", "Wide Appeal", "Obscure A", "Obscure B", "Obscure C")
        )
        service.launchElection(ivy, electionName, allowEdit = false)
        service.castBallot(
            alice,
            "Alice",
            electionName,
            listOf(Ranking("Relatively Unknown", 1), Ranking("Wide Appeal", 2))
        )
        service.castBallot(
            bob,
            "Bob",
            electionName,
            listOf(Ranking("Relatively Unknown", 1), Ranking("Wide Appeal", 2))
        )
        service.castBallot(
            carol,
            "Carol",
            electionName,
            listOf(Ranking("Obscure A", 1), Ranking("Wide Appeal", 2))
        )
        service.castBallot(
            dave,
            "Dave",
            electionName,
            listOf(Ranking("Obscure B", 1), Ranking("Wide Appeal", 2))
        )
        service.castBallot(
            eve,
            "Eve",
            electionName,
            listOf(Ranking("Obscure C", 1), Ranking("Wide Appeal", 2))
        )
        service.finalizeElection(ivy, electionName)
    }

    private fun createPetsElection(service: Service) {
        val electionName = "Pets"
        service.addElection(judy, electionName)
        service.setCandidates(
            judy,
            electionName,
            listOf("Cat", "Dog", "Fish", "Bird", "Snake", "Spider", "Lizard", "Ferret")
        )
        service.launchElection(judy, electionName, allowEdit = false)
        service.castBallot(
            alice,
            "Alice",
            electionName,
            listOf(
                Ranking("Dog", 1),
                Ranking("Cat", 2),
                Ranking("Lizard", 3),
                Ranking("Fish", 4),
                Ranking("Spider", 5),
                Ranking("Snake", 6),
                Ranking("Bird", 7),
                Ranking("Ferret", 8)
            )
        )
        service.castBallot(
            bob,
            "Bob",
            electionName,
            listOf(
                Ranking("Dog", 1),
                Ranking("Snake", 2),
                Ranking("Spider", 3),
                Ranking("Lizard", 4),
                Ranking("Cat", 5),
                Ranking("Fish", 6),
                Ranking("Bird", 7),
                Ranking("Ferret", 8)
            )
        )
        service.castBallot(
            carol,
            "Carol",
            electionName,
            listOf(
                Ranking("Spider", 1),
                Ranking("Snake", 2),
                Ranking("Ferret", 3),
                Ranking("Cat", 4),
                Ranking("Dog", 5),
                Ranking("Lizard", 6),
                Ranking("Bird", 7),
                Ranking("Fish", 8)
            )
        )
        service.castBallot(
            dave,
            "Dave",
            electionName,
            listOf(
                Ranking("Fish", 2),
                Ranking("Bird", 1),
                Ranking("Lizard", 3),
                Ranking("Dog", 4),
                Ranking("Spider", 8),
                Ranking("Ferret", 6),
                Ranking("Cat", 7),
                Ranking("Snake", 5)
            )
        )
        service.finalizeElection(judy, electionName)
    }
}
