package com.seanshubin.condorcet.backend.console

import com.seanshubin.condorcet.backend.console.RegressionNotifications.Phase.ACTUAL
import com.seanshubin.condorcet.backend.console.RegressionNotifications.Phase.EXPECTED
import com.seanshubin.condorcet.backend.dependencies.DeterministicDependencies
import com.seanshubin.condorcet.backend.domain.Role
import com.seanshubin.condorcet.backend.service.http.ServiceCommand
import com.seanshubin.condorcet.backend.service.http.ServiceCommand.*
import kotlin.test.Test

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
            ListUsers
        )
        val tester = Tester()
        tester.generateMissingExpectations(commands)

        // when
        tester.generateActual(commands)

        // then
        tester.compareActualWithExpected()
    }

    class Tester {
        fun generateMissingExpectations(commands: List<ServiceCommand>) {
            val dependencies = DeterministicDependencies(RegressionIntegration(EXPECTED))
            val regressionTestRunner = RegressionTestRunner(dependencies, commands)
            regressionTestRunner.run()
        }

        fun generateActual(commands: List<ServiceCommand>) {
            val dependencies = DeterministicDependencies(RegressionIntegration(ACTUAL))
            val regressionTestRunner = RegressionTestRunner(dependencies, commands)
            regressionTestRunner.run()
        }

        fun compareActualWithExpected() {

        }
    }
}
