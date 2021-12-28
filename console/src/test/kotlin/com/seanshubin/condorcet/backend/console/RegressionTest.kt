package com.seanshubin.condorcet.backend.console

import com.seanshubin.condorcet.backend.console.Phase.ACTUAL
import com.seanshubin.condorcet.backend.console.Phase.EXPECTED
import com.seanshubin.condorcet.backend.dependencies.Dependencies
import com.seanshubin.condorcet.backend.domain.Ranking
import com.seanshubin.condorcet.backend.domain.Role
import com.seanshubin.condorcet.backend.service.http.ServiceCommand
import com.seanshubin.condorcet.backend.service.http.ServiceCommand.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.test.Test
import kotlin.test.assertEquals

class RegressionTest {
    @Test
    fun tryTypicalPathForEveryApiCallAtLeastOnce() {
        // given
        val commands = listOf(
            Register(
                name = "Alice",
                email = "alice@email.com",
                password = "alice-password"
            ),
            Register(
                name = "Bob",
                email = "bob@email.com",
                password = "bob-password"
            ),
            Register(
                name = "Carol",
                email = "carol@email.com",
                password = "carol-password"
            ),
            Logout,
            Authenticate(
                nameOrEmail = "Alice",
                password = "alice-password"
            ),
            Refresh,
            SetRole(
                name = "Bob",
                role = Role.USER
            ),
            RemoveUser(name = "Carol"),
            ListUsers,
            AddElection(name = "Delete Me"),
            AddElection(name = "Favorite Ice Cream Flavor"),
            UpdateElection(
                name = "Favorite Ice Cream Flavor",
                newName = "Favorite Ice Cream",
                secretBallot = true,
                isTemplate = true,
                ownerCanDeleteBallots = true,
                auditorCanDeleteBallots = true,
                restrictWhoCanVote = true,
                clearScheduledStart = false,
                scheduledStart = ZonedDateTime.of(
                    LocalDate.of(2021, 2, 3),
                    LocalTime.of(4, 55, 30),
                    ZoneId.of("UTC")
                ).toInstant(),
                clearScheduledEnd = false,
                scheduledEnd = ZonedDateTime.of(
                    LocalDate.of(2022, 2, 3),
                    LocalTime.of(4, 55, 30),
                    ZoneId.of("UTC")
                ).toInstant(),
                noChangesAfterVote = false,
                isOpen = true
            ),
            SetCandidates(
                electionName = "Favorite Ice Cream",
                candidateNames = listOf("Chocolate", "Vanilla", "Strawberry")
            ),
            CastBallot(
                voterName = "Alice",
                electionName = "Favorite Ice Cream",
                rankings = listOf(
                    Ranking("Vanilla", 1),
                    Ranking("Chocolate", 2)
                )
            ),
            SetCandidates(
                electionName = "Favorite Ice Cream",
                candidateNames = listOf(
                    "Butter Pecan",
                    "Chocolate",
                    "Neapolitan",
                    "Vanilla",
                    "Mint",
                    "Chocolate Chip"
                )
            ),
            ListRankings(
                voterName = "Alice",
                electionName = "Favorite Ice Cream"
            ),
            GetElection(name = "Favorite Ice Cream"),
            DeleteElection(name = "Delete Me"),
            ListElections,
            ListCandidates(
                electionName = "Favorite Ice Cream"
            ),
            ListTables,
            UserCount,
            ElectionCount,
            TableCount,
            EventCount,
            TableData("user"),
            DebugTableData("election"),
            EventData
        )
        val tester = Tester()
        tester.generateMissingExpectations(commands)

        // when
        tester.generateActual(commands)

        // then
        tester.compareActualWithExpected()
    }

    class Tester {
        val regressionIntegrationExpected = RegressionIntegration(EXPECTED)
        val regressionIntegrationActual = RegressionIntegration(ACTUAL)
        fun generateMissingExpectations(commands: List<ServiceCommand>) {
            val dependencies = Dependencies(regressionIntegrationExpected)
            val regressionTestRunner = RegressionTestRunner(dependencies, commands)
            regressionTestRunner.run()
        }

        fun generateActual(commands: List<ServiceCommand>) {
            val dependencies = Dependencies(regressionIntegrationActual)
            val regressionTestRunner = RegressionTestRunner(dependencies, commands)
            regressionTestRunner.run()
        }

        fun compareActualWithExpected() {
            RegressionFile.values().forEach {
                val expected = regressionIntegrationExpected.regressionData.loadText(it)
                val actual = regressionIntegrationActual.regressionData.loadText(it)
                val expectedFilePath = regressionIntegrationExpected.regressionData.fullPath(it)
                val actualFilePath = regressionIntegrationActual.regressionData.fullPath(it)
                assertEquals(
                    expected,
                    actual,
                    "Difference between files\nexpect: $expectedFilePath\nactual: $actualFilePath"
                )
            }
        }
    }
}
