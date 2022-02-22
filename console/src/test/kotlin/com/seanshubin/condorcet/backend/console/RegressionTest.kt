package com.seanshubin.condorcet.backend.console

import com.seanshubin.condorcet.backend.console.Phase.ACTUAL
import com.seanshubin.condorcet.backend.console.Phase.EXPECTED
import com.seanshubin.condorcet.backend.dependencies.Dependencies
import com.seanshubin.condorcet.backend.domain.Ranking
import com.seanshubin.condorcet.backend.domain.Role
import com.seanshubin.condorcet.backend.service.RegexConstants
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
            Health,
            Register(
                userName = "Alice",
                email = "alice@email.com",
                password = "alice-password"
            ),
            Register(
                userName = "Bob",
                email = "bob@email.com",
                password = "bob-password"
            ),
            Register(
                userName = "Carol",
                email = "carol@email.com",
                password = "carol-password"
            ),
            Register(
                userName = "Dave",
                email = "dave@email.com",
                password = "dave-password"
            ),
            Register(
                userName = "Eve",
                email = "eve@email.com",
                password = "eve-password"
            ),
            Logout,
            Authenticate(
                nameOrEmail = "Alice",
                password = "alice-password"
            ),
            Refresh,
            SetRole(
                userName = "Bob",
                role = Role.USER
            ),
            SetRole(
                userName = "Dave",
                role = Role.USER
            ),
            SetRole(
                userName = "Eve",
                role = Role.USER
            ),
            RemoveUser(userName = "Carol"),
            ListUsers,
            AddElection(electionName = "Delete Me"),
            AddElection(electionName = "Favorite Ice Cream Flavor"),
            UpdateElection(
                electionName = "Favorite Ice Cream Flavor",
                newElectionName = "Favorite Ice Cream",
                secretBallot = true,
                clearNoVotingBefore = false,
                noVotingBefore = null,
                clearNoVotingAfter = false,
                noVotingAfter = null
            ),
            SetCandidates(
                electionName = "Favorite Ice Cream",
                candidateNames = listOf("Chocolate", "Vanilla", "Strawberry")
            ),
            SetEligibleVoters(
                electionName = "Favorite Ice Cream",
                userNames = listOf("Alice", "Bob", "Dave")
            ),
            ListEligibility(
                electionName = "Favorite Ice Cream"
            ),
            IsEligible(
                electionName = "Favorite Ice Cream",
                userName = "Alice"
            ),
            LaunchElection(
                electionName = "Favorite Ice Cream",
                allowEdit = true
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
            CastBallot(
                voterName = "Alice",
                electionName = "Favorite Ice Cream",
                rankings = listOf(
                    Ranking("Chocolate Chip", 1),
                    Ranking("Neapolitan", 2),
                    Ranking("Chocolate", 3),
                    Ranking("Vanilla", 4),
                    Ranking("Butter Pecan", 5),
                    Ranking("Mint", 6),
                )
            ),
            ListRankings(
                voterName = "Alice",
                electionName = "Favorite Ice Cream"
            ),
            GetElection(electionName = "Favorite Ice Cream"),
            DeleteElection(electionName = "Delete Me"),
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
            EventData,
            Authenticate(
                nameOrEmail = "Bob",
                password = "bob-password"
            ),
            CastBallot(
                voterName = "Bob",
                electionName = "Favorite Ice Cream",
                rankings = listOf(
                    Ranking("Chocolate", 1),
                    Ranking("Chocolate Chip", 2),
                    Ranking("Vanilla", 3),
                    Ranking("Mint", 4),
                    Ranking("Butter Pecan", 5),
                    Ranking("Neapolitan", 6),
                )
            ),
            Authenticate(
                nameOrEmail = "Dave",
                password = "dave-password"
            ),
            CastBallot(
                voterName = "Dave",
                electionName = "Favorite Ice Cream",
                rankings = listOf(
                    Ranking("Mint", 1),
                    Ranking("Chocolate Chip", 2),
                    Ranking("Neapolitan", 3),
                    Ranking("Chocolate", 4),
                    Ranking("Vanilla", 5),
                    Ranking("Butter Pecan", 6),
                )
            ),
            Authenticate(
                nameOrEmail = "Alice",
                password = "alice-password"
            ),
            FinalizeElection(
                electionName = "Favorite Ice Cream"
            ),
            Tally(
                electionName = "Favorite Ice Cream"
            ),
            GetBallot(voterName = "Alice", electionName = "Favorite Ice Cream")
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
            val dependencies = Dependencies(arrayOf(), regressionIntegrationExpected)
            val regressionTestRunner = RegressionTestRunner(dependencies, commands)
            regressionTestRunner.run()
        }

        fun generateActual(commands: List<ServiceCommand>) {
            val dependencies = Dependencies(arrayOf(), regressionIntegrationActual)
            val regressionTestRunner = RegressionTestRunner(dependencies, commands)
            regressionTestRunner.run()
        }

        private val whitespaceBlock = Regex("""\s+""")
        private val newLineRegex = Regex("""\r\n|\r|\n""")
        private val tableCharsRegex = Regex("[║│╔═╗╤╚╝╧╟─╢┼]")

        private fun String.replaceTableCharsWithWhitespace(): String = replace(tableCharsRegex, " ")

        private fun String.collapseWhitespace(): String =
            replace(RegexConstants.whitespaceBlock, " ")

        private fun String.collapseMultilineWhitespace(): String =
            split(newLineRegex).joinToString("\n") { it.replaceTableCharsWithWhitespace().collapseWhitespace() }

        fun compareActualWithExpected() {
            RegressionFile.values().forEach {
                val expected = regressionIntegrationExpected.regressionData.loadText(it).collapseMultilineWhitespace()
                val actual = regressionIntegrationActual.regressionData.loadText(it).collapseMultilineWhitespace()
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
