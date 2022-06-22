package com.seanshubin.condorcet.backend.jwt

import java.time.Clock
import java.util.*

class JwtClock(private val clock: Clock) : com.auth0.jwt.interfaces.Clock {
    override fun getToday(): Date = Date.from(clock.instant())
}
