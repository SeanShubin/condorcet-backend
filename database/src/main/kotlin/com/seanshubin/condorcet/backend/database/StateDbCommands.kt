package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.domain.ElectionUpdates
import com.seanshubin.condorcet.backend.domain.Role

interface StateDbCommands {
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

    fun setCandidates(authority: String, electionName: String, candidateNames: List<String>)
}
