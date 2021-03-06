package com.seanshubin.condorcet.backend.dependencies

import com.seanshubin.condorcet.backend.crypto.UniqueIdGenerator
import com.seanshubin.condorcet.backend.genericdb.GenericTable
import com.seanshubin.condorcet.backend.http.RequestValue
import com.seanshubin.condorcet.backend.http.ResponseValue
import com.seanshubin.condorcet.backend.server.Notifications
import java.nio.file.Path
import java.time.Clock

interface Integration {
    val createLoggingNotifications: (Path) -> Notifications
    val host: String
    val user: String
    val password: String
    val eventSchemaName: String
    val stateSchemaName: String
    val rootDatabaseEvent: (String) -> Unit
    val eventDatabaseEvent: (String) -> Unit
    val stateDatabaseEvent: (String) -> Unit
    val eventTableEvent: (GenericTable) -> Unit
    val stateTableEvent: (GenericTable) -> Unit
    val requestEvent: (RequestValue) -> Unit
    val responseEvent: (ResponseValue) -> Unit
    val uniqueIdGenerator: UniqueIdGenerator
    val clock: Clock
    val whereKeysAreStored: Path
}
