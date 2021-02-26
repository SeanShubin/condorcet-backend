package com.seanshubin.condorcet.backend.service.http

import com.seanshubin.condorcet.backend.domain.ElectionUpdates

object DataTransfer {
    fun ServiceCommand.UpdateElection.toElectionConfig(): ElectionUpdates =
        ElectionUpdates(
            newName,
            secretBallot,
            clearScheduledStart,
            scheduledStart,
            clearScheduledEnd,
            scheduledEnd,
            restrictWhoCanVote,
            ownerCanDeleteBallots,
            auditorCanDeleteBallots,
            isTemplate,
            noChangesAfterVote,
            isOpen
        )
}
