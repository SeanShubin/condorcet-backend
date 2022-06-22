package com.seanshubin.condorcet.backend.server

import com.seanshubin.condorcet.backend.contract.FilesContract
import com.seanshubin.condorcet.backend.database.ImmutableDbOperations
import com.seanshubin.condorcet.backend.genericdb.ConnectionWrapper
import com.seanshubin.condorcet.backend.genericdb.Lifecycle
import java.io.PrintWriter
import java.nio.charset.Charset
import java.nio.file.Path
import java.nio.file.StandardOpenOption

class BackupRunner(
    private val backupFilePath: Path,
    private val charset: Charset,
    private val files: FilesContract,
    private val eventConnectionLifecycle: Lifecycle<ConnectionWrapper>,
    private val stateConnectionLifecycle: Lifecycle<ConnectionWrapper>,
    private val createImmutableDbOperations: (ConnectionWrapper, ConnectionWrapper) -> ImmutableDbOperations
) : Runnable {
    override fun run() {
        eventConnectionLifecycle.withValue { eventConnection ->
            stateConnectionLifecycle.withValue { stateConnection ->
                val (immutableDbQueries) = createImmutableDbOperations(eventConnection, stateConnection)
                files.newBufferedWriter(backupFilePath, charset, StandardOpenOption.CREATE_NEW).use { bufferedWriter ->
                    PrintWriter(bufferedWriter).use { printWriter ->
                        immutableDbQueries.backupToWriter(printWriter)
                    }
                }
            }
        }
    }
}
