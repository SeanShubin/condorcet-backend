package com.seanshubin.condorcet.backend.server

import com.seanshubin.condorcet.backend.contract.FilesContract
import com.seanshubin.condorcet.backend.database.Event
import com.seanshubin.condorcet.backend.database.ImmutableDbOperations
import com.seanshubin.condorcet.backend.genericdb.ConnectionWrapper
import com.seanshubin.condorcet.backend.genericdb.Lifecycle
import com.seanshubin.condorcet.backend.genericdb.SchemaCreator
import java.nio.charset.Charset
import java.nio.file.Path
import java.sql.SQLException

class RestoreRunner(
    private val backupFilePath: Path,
    private val charset: Charset,
    private val files: FilesContract,
    private val rootConnectionLifecycle: Lifecycle<ConnectionWrapper>,
    private val eventConnectionLifecycle: Lifecycle<ConnectionWrapper>,
    private val stateConnectionLifecycle: Lifecycle<ConnectionWrapper>,
    private val createImmutableDbOperations: (ConnectionWrapper, ConnectionWrapper) -> ImmutableDbOperations,
    private val createSchemaCreator: (ConnectionWrapper) -> SchemaCreator
) : Runnable {
    override fun run() {
        if (!files.exists(backupFilePath)) return
        rootConnectionLifecycle.withValue { rootConnection ->
            handleMissingDatabase(rootConnection, tryAgain = true) {
                eventConnectionLifecycle.withValue { eventConnection ->
                    stateConnectionLifecycle.withValue { stateConnection ->
                        val (immutableDbQueries, immutableDbCommands) = createImmutableDbOperations(
                            eventConnection,
                            stateConnection
                        )
                        val eventCount = immutableDbQueries.eventCount()
                        if (eventCount > 0) {
                            println("Not restoring from $backupFilePath, immutable database not empty, it has $eventCount events")
                        } else {
                            files.newBufferedReader(backupFilePath, charset).use { bufferedReader ->
                                var line: String? = bufferedReader.readLine()
                                while (line != null) {
                                    if (line.isBlank()) continue
                                    val event = Event.fromLine(line)
                                    immutableDbCommands.addEvent(
                                        event.authority,
                                        event.type,
                                        event.text,
                                        event.whenHappened
                                    )
                                    line = bufferedReader.readLine()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun handleMissingDatabase(rootConnection: ConnectionWrapper, tryAgain: Boolean, f: () -> Unit) {
        try {
            f()
        } catch (ex: SQLException) {
            if (tryAgain) {
                if (SchemaCreator.isDatabaseMissing(ex)) {
                    val schemaCreator = createSchemaCreator(rootConnection)
                    schemaCreator.initialize()
                }
                handleMissingDatabase(rootConnection, tryAgain = false, f)
            } else {
                throw ex
            }
        }

    }
}
