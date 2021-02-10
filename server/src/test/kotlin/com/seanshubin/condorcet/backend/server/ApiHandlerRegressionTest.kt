package com.seanshubin.condorcet.backend.server

import com.seanshubin.condorcet.backend.domain.Role
import com.seanshubin.condorcet.backend.json.JsonMappers
import com.seanshubin.condorcet.backend.service.AccessToken
import com.seanshubin.condorcet.backend.service.ServiceRequest
import org.junit.Test
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.test.assertEquals

class ApiHandlerRegressionTest {
    @Test
    fun regressionTest() {
        // given
        val aliceAccessToken = AccessToken("Alice", Role.OWNER)
        val aliceAccessTokenString = JsonMappers.compact.writeValueAsString(aliceAccessToken)
        val aliceAuthorizationHeader = Pair("Authorization", "Bearer $aliceAccessTokenString")
        val aliceAuthorizationHeaders = listOf(aliceAuthorizationHeader)
        val commands = listOf(
            RequestEvent(
                ServiceRequest.Register(
                    name = "Alice",
                    email = "alice@email.com",
                    password = "alice-password"
                )
            ),
            RequestEvent(
                ServiceRequest.Register(
                    name = "duplicate-email",
                    email = "alice@email.com",
                    password = "alice-password"
                )
            ),
            RequestEvent(
                ServiceRequest.Register(
                    name = "Alice",
                    email = "duplicate@name.com",
                    password = "alice-password"
                )
            ),
            RequestEvent(ServiceRequest.Register(name = "Bob", email = "bob@email.com", password = "bob-password")),
            RequestEvent(
                ServiceRequest.Register(
                    name = "Carol",
                    email = "carol@email.com",
                    password = "carol-password"
                )
            ),
            RequestEvent(ServiceRequest.Register(name = "Dave", email = "dave@email.com", password = "dave-password")),
            RequestEvent(ServiceRequest.SetRole(name = "Bob", role = Role.USER), aliceAuthorizationHeaders),
            RequestEvent(ServiceRequest.Authenticate(nameOrEmail = "Alice", password = "alice-password")),
            RequestEvent(ServiceRequest.Authenticate(nameOrEmail = "alice@email.com", password = "alice-password")),
            RequestEvent(ServiceRequest.Authenticate(nameOrEmail = "Alice", password = "wrong-password")),
            RequestEvent(ServiceRequest.Authenticate(nameOrEmail = "alice@email.com", password = "wrong-password")),
            RequestEvent(ServiceRequest.Authenticate(nameOrEmail = "Nobody", password = "password")),
            RequestEvent(ServiceRequest.Refresh),
            RequestEvent(ServiceRequest.RemoveUser(name = "Dave"), aliceAuthorizationHeaders),
            RequestEvent(ServiceRequest.ListUsers, aliceAuthorizationHeaders)
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
