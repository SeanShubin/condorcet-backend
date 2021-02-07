package com.seanshubin.condorcet.backend.server

import com.seanshubin.condorcet.backend.service.ServiceRequest
import org.junit.Test
import java.io.BufferedReader
import java.io.PrintWriter
import java.io.StringReader
import java.io.StringWriter
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.test.assertEquals

class ApiHandlerRegressionTest {

    @Test
    fun regressionTest() {
        // given
        val commands = listOf(
            ServiceRequest.AddUser(name = "Alice", email = "alice@email.com", password = "alice-password"),
            ServiceRequest.AddUser(name = "duplicate-email", email = "alice@email.com", password = "alice-password"),
            ServiceRequest.AddUser(name = "Alice", email = "duplicate@name.com", password = "alice-password"),
            ServiceRequest.AddUser(name = "Bob", email = "bob@email.com", password = "bob-password"),
            ServiceRequest.AddUser(name = "Carol", email = "carol@email.com", password = "carol-password"),
            ServiceRequest.AddUser(name = "Dave", email = "dave@email.com", password = "dave-password"),
            ServiceRequest.Authenticate(nameOrEmail = "Alice", password = "alice-password"),
            ServiceRequest.Authenticate(nameOrEmail = "alice@email.com", password = "alice-password"),
            ServiceRequest.Authenticate(nameOrEmail = "Alice", password = "wrong-password"),
            ServiceRequest.Authenticate(nameOrEmail = "alice@email.com", password = "wrong-password"),
            ServiceRequest.Authenticate(nameOrEmail = "Nobody", password = "password")
        )
        val snapshotDir = Paths.get("src", "test", "resources")
        val tester = Tester(snapshotDir, commands)
        tester.createMissingSnapshotsForExpected()

        // when
        tester.createSnapshotsForActual()

        // then
        tester.validateExpectationsAgainstActual(snapshotDir)
    }

    class RequestStub(
        private val name: String,
        private val theMethod: String,
        private val body: String
    ) : HttpServletRequestNotImplemented() {
        private val stringReader = StringReader(body)
        private val theReader = BufferedReader(stringReader)
        override fun getMethod(): String = theMethod
        override fun getReader(): BufferedReader {
            return theReader
        }
    }

    class ResponseStub : HttpServletResponseNotImplemented() {
        var theContentType: String? = null
        var theStatus: Int? = null
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)
        override fun setContentType(type: String?) {
            theContentType = type
        }

        override fun getStatus(): Int {
            return theStatus!!
        }

        override fun setStatus(sc: Int) {
            theStatus = sc
        }

        override fun getWriter(): PrintWriter {
            return printWriter
        }
    }


    class Tester(
        private val snapshotDir: Path,
        private val serviceRequests: List<ServiceRequest>
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
            DeterministicDependencies(snapshotDir, snapshotViews, serviceRequests).regressionTestRunner

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
