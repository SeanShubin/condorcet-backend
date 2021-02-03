package com.seanshubin.condorcet.backend.server

import java.nio.file.Files
import java.nio.file.Path

interface SnapshotView {
    val name: String
    fun getPath(snapshotDir: Path, annotation: String): Path {
        return snapshotDir.resolve("regression-$name-$annotation.txt")
    }

    fun getLines(info: Snapshot): List<String>
    fun loadLines(snapshotDir: Path, annotation: String): List<String> {
        return Files.readAllLines(getPath(snapshotDir, annotation))
    }

    object Api : SnapshotView {
        override val name = "api"

        override fun getLines(info: Snapshot): List<String> {
            return info.events.flatMap { it.toLines() }
        }
    }

    object Event : SnapshotView {
        override val name = "event"
        override fun getLines(info: Snapshot): List<String> {
            return info.eventTables.flatMap { it.toLines() }
        }
    }

    object State : SnapshotView {
        override val name = "state"
        override fun getLines(info: Snapshot): List<String> {
            return info.stateTables.flatMap { it.toLines() }
        }
    }

    object EventSql : SnapshotView {
        override val name = "sql-event"
        override fun getLines(info: Snapshot): List<String> {
            return info.sqlEventStatements.map { "$it;" }
        }
    }

    object StateSql : SnapshotView {
        override val name = "sql-state"
        override fun getLines(info: Snapshot): List<String> {
            return info.sqlStateStatements.map { "$it;" }
        }
    }
}
