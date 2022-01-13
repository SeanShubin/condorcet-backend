package com.seanshubin.condorcet.backend.service

import com.seanshubin.condorcet.backend.database.ElectionRow
import com.seanshubin.condorcet.backend.domain.ElectionDetail
import com.seanshubin.condorcet.backend.domain.ElectionSummary

object DataTransfer {
    fun ElectionRow.toDomain(): ElectionSummary =
        ElectionSummary(
            owner,
            name,
            secretBallot,
            noVotingBefore,
            noVotingAfter,
            restrictWhoCanVote,
            ownerCanDeleteBallots,
            auditorCanDeleteBallots,
            isTemplate,
            allowEdit,
            allowVote
        )
    fun ElectionRow.toDomain(candidateCount:Int, voterCount:Int): ElectionDetail =
        ElectionDetail(
            owner,
            name,
            candidateCount,
            voterCount,
            secretBallot,
            noVotingBefore,
            noVotingAfter,
            restrictWhoCanVote,
            ownerCanDeleteBallots,
            auditorCanDeleteBallots,
            isTemplate,
            allowEdit,
            allowVote
        )
}
