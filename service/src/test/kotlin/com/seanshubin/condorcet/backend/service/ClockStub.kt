package com.seanshubin.condorcet.backend.service

import java.time.Clock
import java.time.Instant
import java.time.ZoneId

class ClockStub : Clock() {
    override fun instant(): Instant {
        throw UnsupportedOperationException("not implemented")
    }

    override fun withZone(zone: ZoneId?): Clock {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getZone(): ZoneId {
        throw UnsupportedOperationException("not implemented")
    }
}