package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.domain.ElectionUpdates
import com.seanshubin.condorcet.backend.domain.Ranking
import com.seanshubin.condorcet.backend.domain.Role
import com.seanshubin.condorcet.backend.genericdb.GenericDatabase
import java.time.Instant

class StateCommandsImpl(
    genericDatabase: GenericDatabase
) : StateCommands, GenericDatabase by genericDatabase {
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
        val updatedName = updates.newElectionName ?: name
        if (updates.newElectionName != null) {
            update("election-update-name", updates.newElectionName, name)
        }
        if (updates.secretBallot != null) {
            update("election-update-secret-ballot", updates.secretBallot, updatedName)
        }
        if (updates.clearNoVotingBefore == true) {
            update("election-update-no-voting-before", null, updatedName)
        } else if(updates.noVotingBefore != null) {
            update("election-update-no-voting-before", updates.noVotingBefore, updatedName)
        }
        if (updates.clearNoVotingAfter == true) {
            update("election-update-no-voting-after", null, updatedName)
        } else if(updates.noVotingAfter != null) {
            update("election-update-no-voting-after", updates.noVotingAfter, updatedName)
        }
        if (updates.allowEdit != null) {
            update("election-update-allow-edit", updates.allowEdit, updatedName)
        }
        if (updates.allowVote != null) {
            update("election-update-allow-vote", updates.allowVote, updatedName)
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

    override fun addVoters(authority: String, electionName: String, voterNames: List<String>) {
        voterNames.forEach { voterName ->
            update("voter-insert", electionName, voterName)
        }
    }

    override fun removeVoters(authority: String, electionName: String, voterNames: List<String>) {
        voterNames.forEach { voterName ->
            update("voter-delete", electionName, voterName)
        }
    }

    override fun castBallot(authority: String, voterName: String, electionName: String, rankings: List<Ranking>, confirmation:String, now:Instant) {
        update("ballot-insert", voterName, electionName, confirmation, now)
        setRankings(authority, confirmation, electionName, rankings)
    }

    override fun updateWhenCast(authority: String, confirmation: String, now: Instant) {
        update("ballot-update-when-cast", now, confirmation)
    }

    override fun setRankings(
        authority: String,
        confirmation: String,
        electionName: String,
        rankings: List<Ranking>
    ) {
        update("ranking-delete-by-confirmation", confirmation)
        rankings.forEach { (candidateName, rank) ->
            if (rank != null) {
                update("ranking-insert", confirmation, electionName, candidateName, rank)
            }
        }
    }
}
