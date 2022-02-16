package com.seanshubin.condorcet.backend.dependencies

import com.seanshubin.condorcet.backend.crypto.UniqueIdGenerator
import com.seanshubin.condorcet.backend.genericdb.GenericTable
import com.seanshubin.condorcet.backend.http.RequestValue
import com.seanshubin.condorcet.backend.http.ResponseValue
import com.seanshubin.condorcet.backend.server.Notifications
import java.nio.file.Path
import java.sql.SQLException
import java.time.Clock
import kotlin.random.Random

interface Integration {
    val configurationPath:Path
    val secretsConfigurationPath: Path
    val whereKeysAreStored:Path
    val createLoggingNotifications: (Path) -> Notifications
    val rootDatabaseEvent: (String) -> Unit
    val eventDatabaseEvent: (String) -> Unit
    val stateDatabaseEvent: (String) -> Unit
    val sqlException:(String, String, SQLException) -> Unit
    val eventTableEvent: (GenericTable) -> Unit
    val stateTableEvent: (GenericTable) -> Unit
    val httpRequestEvent: (RequestValue) -> Unit
    val httpResponseEvent: (ResponseValue) -> Unit
    val serviceRequestEvent: (String, String) -> Unit
    val serviceResponseEvent: (String, String, String) -> Unit
    val topLevelException: (Throwable) -> Unit
    val uniqueIdGenerator: UniqueIdGenerator
    val clock: Clock
    val random: Random
}
