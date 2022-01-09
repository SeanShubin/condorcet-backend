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
        update("set-last-synced", lastSynced)
    }

    override fun initializeLastSynced(lastSynced: Int) {
        update("initialize-last-synced", lastSynced)
    }

    override fun createUser(authority: String, name: String, email: String, salt: String, hash: String, role: Role) {
        update("create-user", name, email, salt, hash, role.name)
    }

    override fun setRole(authority: String, name: String, role: Role) {
        update("set-role", role.name, name)
    }

    override fun removeUser(authority: String, name: String) {
        update("delete-user", name)
    }

    override fun addElection(authority: String, owner: String, name: String) {
        update("add-election", owner, name)
    }

    override fun updateElection(authority: String, name: String, updates: ElectionUpdates) {
        if (updates.newName != null) {
            update("set-election-name", updates.newName, name)
        }
        if (updates.secretBallot != null) {
            update("set-election-secret-ballot", updates.secretBallot, name)
        }
        if (updates.clearNoVotingBefore == true) {
            update("set-election-no-voting-before", null, name)
        } else {
            update("set-election-no-voting-before", updates.noVotingBefore, name)
        }
        if (updates.clearNoVotingAfter == true) {
            update("set-election-no-voting-after", null, name)
        } else {
            update("set-election-no-voting-after", updates.noVotingAfter, name)
        }
        if (updates.restrictWhoCanVote != null) {
            update("set-election-restrict-who-can-vote", updates.restrictWhoCanVote, name)
        }
        if (updates.ownerCanDeleteBallots != null) {
            update("set-election-owner-can-delete-ballots", updates.ownerCanDeleteBallots, name)
        }
        if (updates.auditorCanDeleteBallots != null) {
            update("set-election-auditor-can-delete-ballots", updates.auditorCanDeleteBallots, name)
        }
        if (updates.isTemplate != null) {
            update("set-election-is-template", updates.isTemplate, name)
        }
        if (updates.allowChangesAfterVote != null) {
            update("set-election-allow-changes-after-vote", updates.allowChangesAfterVote, name)
        }
        if (updates.isOpen != null) {
            update("set-election-is-open", updates.isOpen, name)
        }
    }

    override fun deleteElection(authority: String, name: String) {
        update("delete-election", name)
    }

    override fun addCandidates(authority: String, electionName: String, candidateNames: List<String>) {
        candidateNames.forEach { candidateName ->
            update("add-candidate-to-election", electionName, candidateName)
        }
    }

    override fun removeCandidates(authority: String, electionName: String, candidateNames: List<String>) {
        candidateNames.forEach { candidateName ->
            update("remove-candidate-from-election", electionName, candidateName)
        }
    }

    override fun castBallot(authority: String, voterName: String, electionName: String, rankings: List<Ranking>) {
        val now = clock.instant()
        val uniqueId = uniqueIdGenerator.uniqueId()
        update("create-ballot", voterName, electionName, uniqueId, now)
        rankings.forEach { (candidateName, rank) ->
            if (rank == null) {
                update("delete-ranking", voterName, electionName, candidateName)
            } else {
                update("create-ranking", voterName, electionName, electionName, candidateName, rank)
            }
        }
    }

    override fun rescindBallot(authority: String, voterName: String, electionName: String) {
        update("delete-rankings", voterName, electionName)
        update("delete-ballot", voterName, electionName)
    }
}
