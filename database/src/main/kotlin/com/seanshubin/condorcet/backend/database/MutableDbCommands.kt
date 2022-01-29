package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.domain.ElectionUpdates
import com.seanshubin.condorcet.backend.domain.Ranking
import com.seanshubin.condorcet.backend.domain.Role
import java.time.Instant

interface MutableDbCommands {
    fun setLastSynced(lastSynced: Int)

    fun initializeLastSynced(lastSynced: Int)

    fun createUser(
        authority: String,
        name: String,
        email: String,
        salt: String,
        hash: String,
        role: Role
    )

    fun setRole(authority: String, name: String, role: Role)

    fun removeUser(authority: String, name: String)

    fun addElection(authority: String, owner: String, name: String)

    fun updateElection(authority: String, name: String, updates: ElectionUpdates)

    fun deleteElection(authority: String, name: String)

    fun addCandidates(authority: String, electionName: String, candidateNames: List<String>)

    fun removeCandidates(authority: String, electionName: String, candidateNames: List<String>)

    fun addVoters(authority: String, electionName: String, voterNames: List<String>)

    fun removeVoters(authority: String, electionName: String, voterNames: List<String>)

    fun castBallot(authority: String, voterName: String, electionName: String, rankings: List<Ranking>, confirmation:String, now: Instant)

    fun setRankings(authority: String, confirmation:String, electionName:String, rankings: List<Ranking>)

    fun updateWhenCast(authority:String, confirmation: String, now:Instant)
}