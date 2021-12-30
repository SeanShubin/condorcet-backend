package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.genericdb.DbRow
import java.time.Instant

data class BallotRow(val user:String,
                     val election:String,
                     val confirmation:String,
                     val whenCast: Instant): DbRow {
    override val cells: List<Any?>
        get() = listOf(user, election, confirmation, whenCast)
}
