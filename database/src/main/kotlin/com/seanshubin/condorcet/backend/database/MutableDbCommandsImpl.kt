package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.domain.ElectionUpdates
import com.seanshubin.condorcet.backend.domain.Ranking
import com.seanshubin.condorcet.backend.domain.Role
import com.seanshubin.condorcet.backend.genericdb.GenericDatabase
import java.time.Instant

class MutableDbCommandsImpl(
    genericDatabase: GenericDatabase
) : MutableDbCommands, GenericDatabase by genericDatabase {
    override fun setLastSynced(lastSynced: Int) {
        update("variable-update-last-synced", lastSynced)
    }

    override fun initializeLastSynced(lastSynced: Int) {
        update("variable-insert-last-synced", lastSynced)
    }

    override fun createUser(
        authority: String,
        userName: String,
        email: String,
        salt: String,
        hash: String,
        role: Role
    ) {
        update("user-insert", userName, email, salt, hash, role.name)
    }

    override fun setRole(authority: String, userName: String, role: Role) {
        update("user-update-role", role.name, userName)
    }

    override fun removeUser(authority: String, userName: String) {
        update("delete-user", userName)
    }

    override fun addElection(authority: String, owner: String, electionName: String) {
        update("election-insert", owner, electionName)
    }

    override fun updateElection(authority: String, electionName: String, updates: ElectionUpdates) {
        val updatedName = updates.newElectionName ?: electionName
        if (updates.newElectionName != null) {
            update("election-update-name", updates.newElectionName, electionName)
        }
        if (updates.secretBallot != null) {
            update("election-update-secret-ballot", updates.secretBallot, updatedName)
        }
        if (updates.clearNoVotingBefore == true) {
            update("election-update-no-voting-before", null, updatedName)
        } else if (updates.noVotingBefore != null) {
            update("election-update-no-voting-before", updates.noVotingBefore, updatedName)
        }
        if (updates.clearNoVotingAfter == true) {
            update("election-update-no-voting-after", null, updatedName)
        } else if (updates.noVotingAfter != null) {
            update("election-update-no-voting-after", updates.noVotingAfter, updatedName)
        }
        if (updates.allowEdit != null) {
            update("election-update-allow-edit", updates.allowEdit, updatedName)
        }
        if (updates.allowVote != null) {
            update("election-update-allow-vote", updates.allowVote, updatedName)
        }
    }

    override fun deleteElection(authority: String, electionName: String) {
        update("delete-election", electionName)
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

    override fun castBallot(
        authority: String,
        voterName: String,
        electionName: String,
        rankings: List<Ranking>,
        confirmation: String,
        now: Instant
    ) {
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

    override fun setPassword(authority: String, userName: String, salt: String, hash: String) {
        update("user-update-password", salt, hash, userName)
    }

    override fun setUserName(authority: String, oldUserName: String, newUserName: String) {
        update("user-update-name", newUserName, oldUserName)
    }

    override fun setEmail(authority: String, userName: String, email: String) {
        update("user-update-email", email, userName)
    }
}
