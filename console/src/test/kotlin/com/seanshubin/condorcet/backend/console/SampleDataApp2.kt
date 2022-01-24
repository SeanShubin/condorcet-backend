package com.seanshubin.condorcet.backend.console

import com.seanshubin.condorcet.backend.dependencies.Dependencies
import com.seanshubin.condorcet.backend.domain.ElectionUpdates
import com.seanshubin.condorcet.backend.domain.Ranking
import com.seanshubin.condorcet.backend.domain.Role
import com.seanshubin.condorcet.backend.domain.Role.*
import com.seanshubin.condorcet.backend.service.AccessToken
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

object SampleDataApp2 {
    @JvmStatic
    fun main(args: Array<String>) {
        val dependencies = Dependencies(ConsoleIntegration())
        val initializer = dependencies.schemaCreator
        val service = dependencies.service
        initializer.purgeAllData()
        initializer.initialize()
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

        // Spoiler Alert
        service.addElection(grace, "Spoiler Alert")
        service.setCandidates(grace, "Spoiler Alert", listOf("Minor Improvements","Status Quo","Radical Changes"))
        service.launchElection(grace, "Spoiler Alert", false)
        service.castBallot(alice, "Alice", "Spoiler Alert", listOf(Ranking("Status Quo", 2),Ranking("Radical Changes", null),Ranking("Minor Improvements", 1)))
        service.castBallot(bob, "Bob", "Spoiler Alert", listOf(Ranking("Minor Improvements", 1),Ranking("Status Quo", 2),Ranking("Radical Changes", 3)))
        service.castBallot(carol, "Carol", "Spoiler Alert", listOf(Ranking("Radical Changes", 4),Ranking("Minor Improvements", 1),Ranking("Status Quo", 2)))
        service.castBallot(dave, "Dave", "Spoiler Alert", listOf(Ranking("Minor Improvements", 2),Ranking("Radical Changes", null),Ranking("Status Quo", 1)))
        service.castBallot(eve, "Eve", "Spoiler Alert", listOf(Ranking("Status Quo", 1),Ranking("Radical Changes", 3),Ranking("Minor Improvements", 2)))
        service.castBallot(frank, "Frank", "Spoiler Alert", listOf(Ranking("Radical Changes", 3),Ranking("Minor Improvements", 2),Ranking("Status Quo", 1)))
        service.castBallot(grace, "Grace", "Spoiler Alert", listOf(Ranking("Radical Changes", 1),Ranking("Minor Improvements", 2),Ranking("Status Quo", 3)))
        service.castBallot(heidi, "Heidi", "Spoiler Alert", listOf(Ranking("Status Quo", null),Ranking("Radical Changes", 1),Ranking("Minor Improvements", 2)))
        service.castBallot(ivy, "Ivy", "Spoiler Alert", listOf(Ranking("Minor Improvements", 20),Ranking("Radical Changes", 10),Ranking("Status Quo", 30)))
        service.castBallot(judy, "Judy", "Spoiler Alert", listOf(Ranking("Minor Improvements", 2),Ranking("Status Quo", 3),Ranking("Radical Changes", 1)))

        // Opening Move
        service.addElection(heidi, "Opening Move")
        service.updateElection(heidi, "Opening Move", ElectionUpdates(secretBallot = false))
        service.setCandidates(heidi, "Opening Move", listOf("Rock","Paper","Scissors"))
        service.launchElection(heidi, "Opening Move", allowEdit = false)
        service.castBallot(alice, "Alice", "Opening Move", listOf(Ranking("Rock", 1),Ranking("Paper", 2),Ranking("Scissors", 3)))
        service.castBallot(bob, "Bob", "Opening Move", listOf(Ranking("Rock", 1),Ranking("Paper", 2),Ranking("Scissors", 3)))
        service.castBallot(carol, "Carol", "Opening Move", listOf(Ranking("Rock", 1),Ranking("Paper", 2),Ranking("Scissors", 3)))
        service.castBallot(dave, "Dave", "Opening Move", listOf(Ranking("Paper", 1),Ranking("Scissors", 2),Ranking("Rock", 3)))
        service.castBallot(eve, "Eve", "Opening Move", listOf(Ranking("Paper", 1),Ranking("Scissors", 2),Ranking("Rock", 3)))
        service.castBallot(frank, "Frank", "Opening Move", listOf(Ranking("Paper", 1),Ranking("Scissors", 2),Ranking("Rock", 3)))
        service.castBallot(grace, "Grace", "Opening Move", listOf(Ranking("Scissors", 1),Ranking("Rock", 2),Ranking("Paper", 3)))
        service.castBallot(heidi, "Heidi", "Opening Move", listOf(Ranking("Scissors", 1),Ranking("Rock", 2),Ranking("Paper", 3)))
        service.castBallot(ivy, "Ivy", "Opening Move", listOf(Ranking("Scissors", 1),Ranking("Rock", 2),Ranking("Paper", 3)))
        service.castBallot(judy, "Judy", "Opening Move", listOf(Ranking("Rock", 1),Ranking("Paper", 2),Ranking("Scissors", 3)))
        service.finalizeElection(heidi, "Opening Move")

        // Credible Compromise
        service.addElection(ivy, "Credible Compromise")
        service.updateElection(ivy, "Credible Compromise", ElectionUpdates(secretBallot = false))
        service.setCandidates(ivy, "Credible Compromise", listOf("Relatively Unknown","Wide Appeal","Obscure A", "Obscure B", "Obscure C"))
        service.launchElection(ivy, "Credible Compromise", allowEdit = false)
        service.castBallot(alice, "Alice", "Credible Compromise", listOf(Ranking("Relatively Unknown", 1),Ranking("Wide Appeal", 2)))
        service.castBallot(bob, "Bob", "Credible Compromise", listOf(Ranking("Relatively Unknown", 1),Ranking("Wide Appeal", 2)))
        service.castBallot(carol, "Carol", "Credible Compromise", listOf(Ranking("Obscure A", 1),Ranking("Wide Appeal", 2)))
        service.castBallot(dave, "Dave", "Credible Compromise", listOf(Ranking("Obscure B", 1),Ranking("Wide Appeal", 2)))
        service.castBallot(eve, "Eve", "Credible Compromise", listOf(Ranking("Obscure C", 1),Ranking("Wide Appeal", 2)))
        service.finalizeElection(ivy, "Credible Compromise")

        // Pets
        service.addElection(judy, "Pets")
        service.updateElection(judy, "Pets", ElectionUpdates(secretBallot = false))
        service.setCandidates(judy, "Pets", listOf("Cat", "Dog", "Fish", "Bird", "Snake", "Spider", "Lizard", "Ferret"))
        service.launchElection(judy, "Pets", allowEdit = false)
        service.castBallot(alice, "Alice", "Pets", listOf(Ranking("Dog", 1),Ranking("Cat", 2),Ranking("Lizard", 3),Ranking("Fish", 4),Ranking("Spider", 5),Ranking("Snake", 6),Ranking("Bird", 7),Ranking("Ferret", 8)))
        service.castBallot(bob, "Bob", "Pets", listOf(Ranking("Dog", 1),Ranking("Snake", 2),Ranking("Spider", 3),Ranking("Lizard", 4),Ranking("Cat", 5),Ranking("Fish", 6),Ranking("Bird", 7),Ranking("Ferret", 8)))
        service.castBallot(carol, "Carol", "Pets", listOf(Ranking("Spider", 1),Ranking("Snake", 2),Ranking("Ferret", 3),Ranking("Cat", 4),Ranking("Dog", 5),Ranking("Lizard", 6),Ranking("Bird", 7),Ranking("Fish", 8)))
        service.castBallot(dave, "Dave", "Pets", listOf(Ranking("Fish", 2),Ranking("Bird", 1),Ranking("Lizard", 3),Ranking("Dog", 4),Ranking("Spider", 8),Ranking("Ferret", 6),Ranking("Cat", 7),Ranking("Snake", 5)))
        service.finalizeElection(judy, "Pets")
    }
}
