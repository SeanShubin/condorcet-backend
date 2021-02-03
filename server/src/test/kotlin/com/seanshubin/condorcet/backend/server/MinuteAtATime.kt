package com.seanshubin.condorcet.backend.server

import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class MinuteAtATime : Clock() {
    val baseline = Instant.parse("2020-01-01T00:00:00Z")
    var index = 0L
    fun reset() {
        index = 0
    }

    override fun withZone(zone: ZoneId?): Clock {
        throw UnsupportedOperationException()
    }

    override fun getZone(): ZoneId {
        throw UnsupportedOperationException()
    }

    override fun instant(): Instant {
        val result = baseline.plus(index, ChronoUnit.MINUTES)
        index++
        return result
    }
}