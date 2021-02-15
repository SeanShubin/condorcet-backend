package com.seanshubin.condorcet.backend.console

import com.seanshubin.condorcet.backend.crypto.UniqueIdGenerator
import com.seanshubin.condorcet.backend.crypto.Uuid4
import com.seanshubin.condorcet.backend.dependencies.Integration
import com.seanshubin.condorcet.backend.http.RequestValue
import com.seanshubin.condorcet.backend.http.ResponseValue
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.nio.file.Paths
import java.time.Clock

class RegressionIntegration(phase: String) : Integration {
    val regressionSnapshotDir = Paths.get("src", "test", "resources")
    val clockPath = regressionSnapshotDir.resolve("deterministic-clock.txt")
    val realClock = Clock.systemUTC()
    val realUniqueIdGenerator: UniqueIdGenerator = Uuid4()
    val uniqueIdGeneratorPath = regressionSnapshotDir.resolve("deterministic-unique-id.txt")
    val charset: Charset = StandardCharsets.UTF_8
    val regressionNotifications = RegressionNotifications(regressionSnapshotDir, charset)

    override val host: String = "localhost"
    override val user: String = "root"
    override val password: String = "insecure"
    override val eventSchemaName: String = "condorcet_regression_test_event"
    override val stateSchemaName: String = "condorcet_regression_test_state"
    override val databaseEvent: (String) -> Unit = regressionNotifications.regressionEvent(phase, "database")
    override val requestEvent: (RequestValue) -> Unit = regressionNotifications.regressionEvent(phase, "http")
    override val responseEvent: (ResponseValue) -> Unit = regressionNotifications.regressionEvent(phase, "http")
    override val uniqueIdGenerator: UniqueIdGenerator =
        RememberingUuidGenerator(realUniqueIdGenerator, uniqueIdGeneratorPath)
    override val clock: Clock = RememberingClock(realClock, clockPath)
    override val whereKeysAreStored: Path = regressionSnapshotDir.resolve("keys")
}