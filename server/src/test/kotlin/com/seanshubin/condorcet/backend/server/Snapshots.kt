package com.seanshubin.condorcet.backend.server

import java.nio.file.Files
import java.nio.file.Path

interface Snapshot {
    val name: String
    fun getPath(snapshotDir: Path, annotation: String): Path {
        return snapshotDir.resolve("regression-$name-$annotation.txt")
    }

    fun getLines(info: SnapshotInfo): List<String>
    fun loadLines(snapshotDir: Path, annotation: String): List<String> {
        return Files.readAllLines(getPath(snapshotDir, annotation))
    }

    object Api : Snapshot {
        override val name = "api"

        override fun getLines(info: SnapshotInfo): List<String> {
            return info.events.flatMap { it.toLines() }
        }
    }

    object Event : Snapshot {
        override val name = "event"
        override fun getLines(info: SnapshotInfo): List<String> {
            return info.eventTables.flatMap { it.toLines() }
        }
    }

    object State : Snapshot {
        override val name = "state"
        override fun getLines(info: SnapshotInfo): List<String> {
            return info.stateTables.flatMap { it.toLines() }
        }
    }
}

