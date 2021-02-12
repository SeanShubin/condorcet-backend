package com.seanshubin.condorcet.backend.server

import com.seanshubin.condorcet.backend.domain.Role
import com.seanshubin.condorcet.backend.service.AccessToken
import com.seanshubin.condorcet.backend.service.http.ServiceCommand
import org.junit.Test
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.test.assertEquals

class ApiHandlerRegressionTest {
    @Test
    fun regressionTest() {
        // given
        val aliceAccessToken = AccessToken("Alice", Role.OWNER)
        val commands = listOf(
            RequestEvent(
                ServiceCommand.Register(
                    name = "Alice",
                    email = "alice@email.com",
                    password = "alice-password"
                )
            ),
            RequestEvent(
                ServiceCommand.Register(
                    name = "duplicate-email",
                    email = "alice@email.com",
                    password = "alice-password"
                )
            ),
            RequestEvent(
                ServiceCommand.Register(
                    name = "Alice",
                    email = "duplicate@name.com",
                    password = "alice-password"
                )
            ),
            RequestEvent(ServiceCommand.Register(name = "Bob", email = "bob@email.com", password = "bob-password")),
            RequestEvent(
                ServiceCommand.Register(
                    name = "Carol",
                    email = "carol@email.com",
                    password = "carol-password"
                )
            ),
            RequestEvent(ServiceCommand.Register(name = "Dave", email = "dave@email.com", password = "dave-password")),
            RequestEvent(ServiceCommand.SetRole(name = "Bob", role = Role.USER), aliceAccessToken),
            RequestEvent(ServiceCommand.Authenticate(nameOrEmail = "Alice", password = "alice-password")),
            RequestEvent(ServiceCommand.Authenticate(nameOrEmail = "alice@email.com", password = "alice-password")),
            RequestEvent(ServiceCommand.Authenticate(nameOrEmail = "Alice", password = "wrong-password")),
            RequestEvent(ServiceCommand.Authenticate(nameOrEmail = "alice@email.com", password = "wrong-password")),
            RequestEvent(ServiceCommand.Authenticate(nameOrEmail = "Nobody", password = "password")),
            RequestEvent(ServiceCommand.Refresh),
            RequestEvent(ServiceCommand.RemoveUser(name = "Dave"), aliceAccessToken),
            RequestEvent(ServiceCommand.ListUsers, aliceAccessToken)
        )
        val snapshotDir = Paths.get("src", "test", "resources")
        val tester = Tester(snapshotDir, commands)
        tester.createMissingSnapshotsForExpected()

        // when
        tester.createSnapshotsForActual()

        // then
        tester.validateExpectationsAgainstActual(snapshotDir)
    }

    class Tester(
        private val snapshotDir: Path,
        private val requestEvents: List<RequestEvent>
    ) {
        val snapshotViews = listOf(
            SnapshotView.Api,
            SnapshotView.Event,
            SnapshotView.State,
            SnapshotView.EventSql,
            SnapshotView.StateSql
        )

        fun createMissingSnapshotsForExpected() {
            createRunner().createMissingSnapshotsForExpected()
        }

        fun createSnapshotsForActual() {
            createRunner().createSnapshotsForActual()
        }

        private fun createRunner(): RegressionTestRunner =
            DeterministicDependencies(snapshotDir, snapshotViews, requestEvents).regressionTestRunner

        fun validateExpectationsAgainstActual(snapshotDir: Path) {
            snapshotViews.forEach { validateSnapshotView(snapshotDir, it) }
        }

        fun validateSnapshotView(snapshotDir: Path, snapshotView: SnapshotView) {
            val expect = snapshotView.loadLines(snapshotDir, "expect").joinToString("\n")
            val actual = snapshotView.loadLines(snapshotDir, "actual").joinToString("\n")
            assertEquals(expect, actual, snapshotView.name)
        }
    }
}
