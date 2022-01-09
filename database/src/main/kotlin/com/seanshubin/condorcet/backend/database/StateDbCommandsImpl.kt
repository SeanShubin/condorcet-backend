package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.crypto.UniqueIdGenerator
import com.seanshubin.condorcet.backend.domain.ElectionUpdates
import com.seanshubin.condorcet.backend.domain.Ranking
import com.seanshubin.condorcet.backend.domain.Role
import com.seanshubin.condorcet.backend.genericdb.GenericDatabase
import java.time.Clock

class StateDbCommandsImpl(
    genericDatabase: GenericDatabase,
    private val clock: Clock,
    private val uniqueIdGenerator: UniqueIdGenerator
) : StateDbCommands, GenericDatabase by genericDatabase {
    override fun setLastSynced(lastSynced: Int) {
        update("variable-update-last-synced", lastSynced)
    }

    override fun initializeLastSynced(lastSynced: Int) {
        update("variable-insert-last-synced", lastSynced)
    }

    override fun createUser(authority: String, name: String, email: String, salt: String, hash: String, role: Role) {
        update("user-insert", name, email, salt, hash, role.name)
    }

    override fun setRole(authority: String, name: String, role: Role) {
        update("user-update-role", role.name, name)
    }

    override fun removeUser(authority: String, name: String) {
        update("delete-user", name)
    }

    override fun addElection(authority: String, owner: String, name: String) {
        update("election-insert", owner, name)
    }

    override fun updateElection(authority: String, name: String, updates: ElectionUpdates) {
        if (updates.newName != null) {
            update("election-update-name", updates.newName, name)
        }
        if (updates.secretBallot != null) {
            update("election-update-secret-ballot", updates.secretBallot, name)
        }
        if (updates.clearNoVotingBefore == true) {
            update("election-update-no-voting-before", null, name)
        } else {
            update("election-update-no-voting-before", updates.noVotingBefore, name)
        }
        if (updates.clearNoVotingAfter == true) {
            update("election-update-no-voting-after", null, name)
        } else {
            update("election-update-no-voting-after", updates.noVotingAfter, name)
        }
        if (updates.restrictWhoCanVote != null) {
            update("election-update-restrict-who-can-vote", updates.restrictWhoCanVote, name)
        }
        if (updates.ownerCanDeleteBallots != null) {
            update("election-update-owner-can-delete-ballots", updates.ownerCanDeleteBallots, name)
        }
        if (updates.auditorCanDeleteBallots != null) {
            update("election-update-auditor-can-delete-ballots", updates.auditorCanDeleteBallots, name)
        }
        if (updates.isTemplate != null) {
            update("election-update-is-template", updates.isTemplate, name)
        }
        if (updates.allowChangesAfterVote != null) {
            update("election-update-allow-changes-after-vote", updates.allowChangesAfterVote, name)
        }
        if (updates.isOpen != null) {
            update("election-update-is-open", updates.isOpen, name)
        }
    }

    override fun deleteElection(authority: String, name: String) {
        update("delete-election", name)
    }

    override fun addCandidates(authority: String, electionName: String, candidateNames: List<String>) {
        candidateNames.forEach { candidateName ->
            update("candidate-insert", electionName, candidateName)
        }
    }

    override fun removeCandidates(authority: String, electionName: String, candidateNames: List<String>) {
        candidateNames.forEach { candidateName ->
            update("candidate-delete", electionName, candidateName)
        }
    }

    override fun castBallot(authority: String, voterName: String, electionName: String, rankings: List<Ranking>) {
        val now = clock.instant()
        val uniqueId = uniqueIdGenerator.uniqueId()
        update("ballot-insert", voterName, electionName, uniqueId, now)
        rankings.forEach { (candidateName, rank) ->
            if (rank == null) {
                update("delete-ranking", voterName, electionName, candidateName)
            } else {
                update("ranking-insert", voterName, electionName, electionName, candidateName, rank)
            }
        }
    }

    override fun rescindBallot(authority: String, voterName: String, electionName: String) {
        update("delete-rankings", voterName, electionName)
        update("delete-ballot", voterName, electionName)
    }
}
