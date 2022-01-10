package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.domain.ElectionUpdates
import java.time.Instant

data class DbElectionUpdates(
    val newName: String? = null,
    val secretBallot: Boolean? = null,
    val clearNoVotingBefore: Boolean? = null,
    val noVotingBefore: Instant? = null,
    val clearNoVotingAfter: Boolean? = null,
    val noVotingAfter: Instant? = null,
    val restrictWhoCanVote: Boolean? = null,
    val ownerCanDeleteBallots: Boolean? = null,
    val auditorCanDeleteBallots: Boolean? = null,
    val isTemplate: Boolean? = null,
    val allowVote: Boolean? = null,
    val allowEdit: Boolean? = null
) {
    companion object {
        fun ElectionUpdates.toDbElectionUpdates(): DbElectionUpdates = DbElectionUpdates(
            newName = newName,
            secretBallot = secretBallot,
            clearNoVotingBefore = clearNoVotingBefore,
            noVotingBefore = noVotingBefore,
            clearNoVotingAfter = clearNoVotingAfter,
            noVotingAfter = noVotingAfter,
            restrictWhoCanVote = restrictWhoCanVote,
            ownerCanDeleteBallots = ownerCanDeleteBallots,
            auditorCanDeleteBallots = auditorCanDeleteBallots,
            isTemplate = isTemplate
        )
    }
}
