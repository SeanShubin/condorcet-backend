package com.seanshubin.condorcet.backend.console

import com.seanshubin.condorcet.backend.console.Phase.ACTUAL
import com.seanshubin.condorcet.backend.console.Phase.EXPECTED
import com.seanshubin.condorcet.backend.dependencies.DeterministicDependencies
import com.seanshubin.condorcet.backend.domain.Role
import com.seanshubin.condorcet.backend.service.http.ServiceCommand
import com.seanshubin.condorcet.backend.service.http.ServiceCommand.*
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
            ListTables,
            TableData("user"),
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
            val dependencies = DeterministicDependencies(regressionIntegrationExpected)
            val regressionTestRunner = RegressionTestRunner(dependencies, commands)
            regressionTestRunner.run()
        }

        fun generateActual(commands: List<ServiceCommand>) {
            val dependencies = DeterministicDependencies(regressionIntegrationActual)
            val regressionTestRunner = RegressionTestRunner(dependencies, commands)
            regressionTestRunner.run()
        }

        fun compareActualWithExpected() {
            RegressionFile.values().forEach {
                val expected = regressionIntegrationExpected.regressionFileMap.getValue(it).load()
                val actual = regressionIntegrationActual.regressionFileMap.getValue(it).load()
                assertEquals(expected, actual)
            }
        }
    }
}
