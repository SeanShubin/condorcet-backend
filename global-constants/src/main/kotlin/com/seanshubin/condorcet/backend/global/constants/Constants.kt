package com.seanshubin.condorcet.backend.global.constants

import java.time.Duration
import java.time.temporal.ChronoUnit

object Constants {
    val refreshTokenDuration = Duration.of(12, ChronoUnit.HOURS)
    val accessTokenDuration = Duration.of(1, ChronoUnit.MINUTES)
    val emailAccessTokenDuration = Duration.of(10, ChronoUnit.MINUTES)
}
