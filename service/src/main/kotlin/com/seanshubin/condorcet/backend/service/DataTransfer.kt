package com.seanshubin.condorcet.backend.service

import com.seanshubin.condorcet.backend.database.ElectionRow
import com.seanshubin.condorcet.backend.domain.Election

object DataTransfer {
    fun ElectionRow.toDomain(): Election =
        Election(
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