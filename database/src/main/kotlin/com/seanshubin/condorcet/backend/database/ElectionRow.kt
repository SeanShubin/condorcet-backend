package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.genericdb.DbRow
import java.time.Instant

data class ElectionRow(
    val owner: String,
    val name: String,
    val start: Instant? = null,
    val end: Instant? = null,
    val secret: Boolean = true,
    val restrictedToVoterList:Boolean = false,
    val doneConfiguring: Instant?,
    val template: Boolean = false,
    val started: Boolean = false,
    val finished: Boolean = false,
    val canChangeCandidatesAfterDoneConfiguring: Boolean = false,
    val ownerCanDeleteBallots: Boolean = false,
    val auditorCanDeleteBallots: Boolean = false
) : DbRow<String> {
    override val primaryKey: String = name
    override val cells: List<Any?> = listOf(
        owner,
        name,
        start,
        end,
        secret,
        doneConfiguring,
        template,
        started,
        finished,
        canChangeCandidatesAfterDoneConfiguring,
        ownerCanDeleteBallots,
        auditorCanDeleteBallots
    )
}
