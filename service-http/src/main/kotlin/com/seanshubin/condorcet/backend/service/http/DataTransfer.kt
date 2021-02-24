package com.seanshubin.condorcet.backend.service.http

import com.seanshubin.condorcet.backend.domain.ElectionUpdates

object DataTransfer {
    fun ServiceCommand.UpdateElection.toElectionConfig(): ElectionUpdates =
        ElectionUpdates(
            restrictedToVoterList,
            shouldSetStartTime,
            startTime,
            shouldSetEndTime,
            endTime,
            secretBallot,
            shouldSetWhenDoneConfiguring,
            whenDoneConfiguring,
            isTemplate,
            isStarted,
            isFinished,
            canChangeCandidatesAfterDoneConfiguring,
            ownerCanDeleteBallots,
            auditorCanDeleteBallots
        )
}
