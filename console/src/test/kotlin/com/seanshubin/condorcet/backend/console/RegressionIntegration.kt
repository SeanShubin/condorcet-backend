package com.seanshubin.condorcet.backend.console

import com.seanshubin.condorcet.backend.crypto.UniqueIdGenerator
import com.seanshubin.condorcet.backend.crypto.Uuid4
import com.seanshubin.condorcet.backend.dependencies.Integration
import com.seanshubin.condorcet.backend.genericdb.GenericTable
import com.seanshubin.condorcet.backend.http.RequestValue
import com.seanshubin.condorcet.backend.http.ResponseValue
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.nio.file.Paths
import java.time.Clock

class RegressionIntegration(phase: Phase) : Integration {
    val regressionSnapshotDir = Paths.get("src", "test", "resources")
    val clockPath = regressionSnapshotDir.resolve("deterministic-clock.txt")
    val realClock = Clock.systemUTC()
    val realUniqueIdGenerator: UniqueIdGenerator = Uuid4()
    val uniqueIdGeneratorPath = regressionSnapshotDir.resolve("deterministic-unique-id.txt")
    val charset: Charset = StandardCharsets.UTF_8
    val regressionFileMap: Map<RegressionFile, RegressionInfoFile> =
        RegressionFile.values().map {
            it to it.toRegressionInfoFile(regressionSnapshotDir, charset, phase)
        }.toMap()

    val regressionNotifications = RegressionNotifications(regressionFileMap)

    override val host: String = "localhost"
    override val user: String = "root"
    override val password: String = "insecure"
    override val eventSchemaName: String = "condorcet_regression_test_event_can_be_purged"
    override val stateSchemaName: String = "condorcet_regression_test_state_can_be_purged"
    override val rootDatabaseEvent: (String) -> Unit = regressionNotifications::rootDatabaseEvent
    override val eventDatabaseEvent: (String) -> Unit = regressionNotifications::eventDatabaseEvent
    override val stateDatabaseEvent: (String) -> Unit = regressionNotifications::stateDatabaseEvent
    override val requestEvent: (RequestValue) -> Unit = regressionNotifications::requestEvent
    override val responseEvent: (ResponseValue) -> Unit = regressionNotifications::responseEvent
    override val eventTableEvent: (GenericTable) -> Unit = regressionNotifications::eventTableEvent
    override val stateTableEvent: (GenericTable) -> Unit = regressionNotifications::stateTableEvent
    override val uniqueIdGenerator: UniqueIdGenerator =
        RememberingUuidGenerator(realUniqueIdGenerator, uniqueIdGeneratorPath)
    override val clock: Clock = RememberingClock(realClock, clockPath)
    override val whereKeysAreStored: Path = regressionSnapshotDir.resolve("keys")
}