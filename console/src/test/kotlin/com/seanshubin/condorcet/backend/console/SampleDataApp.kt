package com.seanshubin.condorcet.backend.console

import com.seanshubin.condorcet.backend.dependencies.Dependencies
import com.seanshubin.condorcet.backend.domain.ElectionUpdates
import com.seanshubin.condorcet.backend.domain.Ranking
import com.seanshubin.condorcet.backend.domain.Role
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

object SampleDataApp {
    @JvmStatic
    fun main(args: Array<String>) {
        val dependencies = Dependencies(ConsoleIntegration())
        val initializer = dependencies.schemaCreator
        val service = dependencies.service
        initializer.purgeAllData()
        initializer.initialize()
        val aliceAccessToken = service.register("Alice", "alice@email.com", "pass").accessToken
        service.register("Bob", "bob@email.com", "pass").accessToken
        service.register("Carol", "carol@email.com", "pass").accessToken
        service.register("Dave", "dave@email.com", "pass").accessToken
        service.register("Eve", "eve@email.com", "pass").accessToken
        service.register("Frank", "frank@email.com", "pass").accessToken
        service.register("Grace", "grace@email.com", "pass").accessToken
        service.register("Heidi", "heidi@email.com", "pass").accessToken
        service.register("Ivy", "ivy@email.com", "pass").accessToken
        service.register("Judy", "judy@email.com", "pass").accessToken
        service.register("Mallory", "mallory@email.com", "pass").accessToken
        service.register("Trent", "trent@email.com", "pass").accessToken
        service.register("Walter", "walter@email.com", "pass").accessToken
        service.register("Peggy", "peggy@email.com", "pass").accessToken
        service.register("Victor", "victor@email.com", "pass").accessToken
        service.setRole(aliceAccessToken, "Bob", Role.AUDITOR)
        val bobAccessToken = service.authenticate("Bob", "pass").accessToken
        service.setRole(aliceAccessToken, "Carol", Role.ADMIN)
        val carolAccessToken = service.authenticate("Carol", "pass").accessToken
        service.setRole(aliceAccessToken, "Dave", Role.ADMIN)
        val daveAccessToken = service.authenticate("Dave", "pass").accessToken
        service.setRole(carolAccessToken, "Eve", Role.USER)
        val eveAccessToken = service.authenticate("Eve", "pass").accessToken
        service.setRole(carolAccessToken, "Frank", Role.USER)
        val frankAccessToken = service.authenticate("Frank", "pass").accessToken
        service.setRole(carolAccessToken, "Grace", Role.USER)
        val graceAccessToken = service.authenticate("Grace", "pass").accessToken
        service.setRole(daveAccessToken, "Heidi", Role.USER)
        val heidiAccessToken = service.authenticate("Heidi", "pass").accessToken
        service.setRole(daveAccessToken, "Ivy", Role.USER)
        val ivyAccessToken = service.authenticate("Ivy", "pass").accessToken
        service.setRole(daveAccessToken, "Judy", Role.USER)
        val judyAccessToken = service.authenticate("Judy", "pass").accessToken
        service.setRole(daveAccessToken, "Mallory", Role.OBSERVER)
        service.setRole(daveAccessToken, "Trent", Role.OBSERVER)
        service.setRole(daveAccessToken, "Peggy", Role.OBSERVER)
        service.addElection(aliceAccessToken, "Cycle Test")
        service.setCandidates(aliceAccessToken, "Cycle Test", listOf("Rock", "Paper", "Scissors"))
        service.setEligibleVoters(aliceAccessToken, "Cycle Test", listOf("Alice", "Bob", "Carol", "Dave", "Eve"))
        service.launchElection(aliceAccessToken, "Cycle Test", allowEdit = true)
        service.castBallot(
            aliceAccessToken, "Alice", "Cycle Test",
            listOf(Ranking("Rock", 1), Ranking("Scissors", 2), Ranking("Paper", 3))
        )
        service.castBallot(
            bobAccessToken, "Bob", "Cycle Test",
            listOf(Ranking("Rock", 1), Ranking("Scissors", 2), Ranking("Paper", 3))
        )
        service.castBallot(
            carolAccessToken, "Carol", "Cycle Test",
            listOf(Ranking("Rock", 1), Ranking("Scissors", 2), Ranking("Paper", 3))
        )
        service.castBallot(
            daveAccessToken, "Dave", "Cycle Test",
            listOf(Ranking("Rock", 1), Ranking("Scissors", 2), Ranking("Paper", 3))
        )
        service.castBallot(
            eveAccessToken, "Eve", "Cycle Test",
            listOf(Ranking("Scissors", 1), Ranking("Paper", 2), Ranking("Rock", 3))
        )
        service.castBallot(
            frankAccessToken, "Frank", "Cycle Test",
            listOf(Ranking("Scissors", 1), Ranking("Paper", 2), Ranking("Rock", 3))
        )
        service.castBallot(
            graceAccessToken, "Grace", "Cycle Test",
            listOf(Ranking("Scissors", 1), Ranking("Paper", 2), Ranking("Rock", 3))
        )
        service.castBallot(
            heidiAccessToken, "Heidi", "Cycle Test",
            listOf(Ranking("Paper", 1), Ranking("Rock", 2), Ranking("Scissors", 3))
        )
        service.castBallot(
            ivyAccessToken, "Ivy", "Cycle Test",
            listOf(Ranking("Paper", 1), Ranking("Rock", 2), Ranking("Scissors", 3))
        )
        service.castBallot(
            judyAccessToken, "Judy", "Cycle Test",
            listOf(Ranking("Paper", 1), Ranking("Rock", 2), Ranking("Scissors", 3))
        )

        service.addElection(eveAccessToken, "Favorite Ice Cream")
        service.setCandidates(
            eveAccessToken, "Favorite Ice Cream", listOf(
                "Chocolate Chip",
                "Neapolitan",
                "Chocolate",
                "Vanilla",
                "Butter Pecan",
                "Mint"
            )
        )
        service.updateElection(
            eveAccessToken, "Favorite Ice Cream",
            ElectionUpdates(
                secretBallot = true
            ),
        )
        service.launchElection(eveAccessToken, "Favorite Ice Cream", allowEdit = false)

        service.addElection(frankAccessToken, "Government")
        service.setCandidates(
            frankAccessToken, "Government", listOf(
                "Monarchy",
                "Aristocracy",
                "Democracy"
            )
        )
        service.addElection(frankAccessToken, "Dystopia")
        service.setCandidates(
            frankAccessToken, "Dystopia", listOf(
                "1984",
                "Brave New World",
                "Fahrenheit 451"
            )
        )
        service.setEligibleVoters(frankAccessToken, "Dystopia", listOf("Alice", "Carol", "Eve", "Grace", "Ivy"))
        service.addElection(ivyAccessToken, "Pet")
        service.setCandidates(
            ivyAccessToken, "Pet", listOf(
                "Cat",
                "Dog",
                "Bird",
                "Fish",
                "Reptile"
            )
        )
        service.addElection(judyAccessToken, "Fantasy")
        service.setCandidates(
            judyAccessToken, "Fantasy", listOf(
                "Lord of the Rings",
                "Marvel Cinematic Universe",
                "Harry Potter"
            )
        )
        service.addElection(judyAccessToken, "Science Fiction")
        service.setCandidates(
            judyAccessToken, "Science Fiction", listOf(
                "Babylon 5",
                "Star Trek",
                "Blake's 7",
                "Firefly"
            )
        )
        service.castBallot(
            aliceAccessToken, "Alice", "Favorite Ice Cream", listOf(
                Ranking("Chocolate Chip", 1),
                Ranking("Neapolitan", 2),
                Ranking("Chocolate", 3),
                Ranking("Vanilla", 4),
                Ranking("Butter Pecan", 5),
                Ranking("Mint", 6),
            )
        )
        service.castBallot(
            carolAccessToken, "Carol", "Favorite Ice Cream", listOf(
                Ranking("Chocolate", 1),
                Ranking("Chocolate Chip", 2),
                Ranking("Vanilla", 3),
                Ranking("Mint", 4),
                Ranking("Butter Pecan", 5),
                Ranking("Neapolitan", 6),
            )
        )
        service.castBallot(
            daveAccessToken, "Dave", "Favorite Ice Cream", listOf(
                Ranking("Mint", 1),
                Ranking("Chocolate Chip", 2),
                Ranking("Neapolitan", 3),
                Ranking("Chocolate", 4),
                Ranking("Vanilla", 5),
                Ranking("Butter Pecan", 6),
            )
        )
    }
}