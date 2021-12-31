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
        service.setCandidates(
            eveAccessToken, "Favorite Ice Cream Flavor", listOf(
                "Chocolate Chip",
                "Neapolitan",
                "Chocolate",
                "Vanilla",
                "Butter Pecan",
                "Mint"
            )
        )
        service.updateElection(
            eveAccessToken, "Favorite Ice Cream Flavor",
            ElectionUpdates(
                newName = "Favorite Ice Cream",
                secretBallot = true,
                isTemplate = true,
                ownerCanDeleteBallots = true,
                auditorCanDeleteBallots = true,
                restrictWhoCanVote = true,
                clearScheduledStart = null,
                scheduledStart = ZonedDateTime.of(
                    LocalDate.of(2021, 2, 3),
                    LocalTime.of(4, 55, 30),
                    ZoneId.of("UTC")
                ).toInstant(),
                clearScheduledEnd = null,
                scheduledEnd = ZonedDateTime.of(
                    LocalDate.of(2022, 2, 3),
                    LocalTime.of(4, 55, 30),
                    ZoneId.of("UTC")
                ).toInstant(),
                noChangesAfterVote = false,
                isOpen = true
            ),
        )

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