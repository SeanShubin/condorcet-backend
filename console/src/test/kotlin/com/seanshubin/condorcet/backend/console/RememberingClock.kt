package com.seanshubin.condorcet.backend.console

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.time.Clock
import java.time.Instant
import java.time.ZoneId

class RememberingClock(val backing: Clock, val path: Path) : Clock() {
    var index = 0
    override fun getZone(): ZoneId {
        throw UnsupportedOperationException()
    }

    override fun withZone(zone: ZoneId?): Clock {
        throw UnsupportedOperationException()
    }

    override fun instant(): Instant {
        val previous: MutableList<String> = if (Files.exists(path)) Files.readAllLines(path) else mutableListOf()
        return if (index < previous.size) {
            val result = Instant.parse(previous[index])
            index++
            result
        } else {
            val result = backing.instant()
            previous.add(result.toString())
            index++
            Files.write(path, listOf(result.toString()), StandardOpenOption.CREATE, StandardOpenOption.APPEND)
            result
        }
    }
}
