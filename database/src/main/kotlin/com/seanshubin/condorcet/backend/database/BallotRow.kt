package com.seanshubin.condorcet.backend.database

import java.time.Instant

data class BallotRow(val user:String,
                     val election:String,
                     val confirmation:String,
                     val whenCast: Instant)
