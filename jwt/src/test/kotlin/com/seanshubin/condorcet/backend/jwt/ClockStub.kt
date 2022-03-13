package com.seanshubin.condorcet.backend.jwt

import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class ClockStub: Clock() {
    var currentTime = Clock.systemUTC().instant()
    override fun instant(): Instant {
        return currentTime
    }

    override fun withZone(zone: ZoneId?): Clock {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getZone(): ZoneId {
        throw UnsupportedOperationException("not implemented")
    }

    fun passTime(duration:Duration){
        currentTime = currentTime.plus(duration)
    }

    companion object {
        fun minutes(quantity:Int): Duration = Duration.of(quantity.toLong(), ChronoUnit.MINUTES)
    }
}