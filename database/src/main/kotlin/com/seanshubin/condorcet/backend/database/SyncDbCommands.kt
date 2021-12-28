package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.domain.ElectionUpdates
import com.seanshubin.condorcet.backend.domain.Ranking
import com.seanshubin.condorcet.backend.domain.Role
import com.seanshubin.condorcet.backend.json.JsonMappers

class SyncDbCommands(private val eventDbCommands: EventDbCommands) : StateDbCommands {
    override fun setLastSynced(lastSynced: Int) {
        // do not sync the commands used to sync the commands
    }

    override fun initializeLastSynced(lastSynced: Int) {
        // do not sync the commands used to sync the commands
    }

    override fun createUser(authority: String, name: String, email: String, salt: String, hash: String, role: Role) {
        processEvent(authority, DbEvent.AddUser(name, email, salt, hash, role))
    }

    override fun setRole(authority: String, name: String, role: Role) {
        processEvent(authority, DbEvent.SetRole(name, role))
    }

    override fun removeUser(authority: String, name: String) {
        processEvent(authority, DbEvent.RemoveUser(name))
    }

    override fun addElection(authority: String, owner: String, name: String) {
        processEvent(authority, DbEvent.AddElection(owner, name))
    }

    override fun updateElection(authority: String, name: String, updates: ElectionUpdates) {
        processEvent(authority, DbEvent.UpdateElection(name, updates))
    }

    override fun deleteElection(authority: String, name: String) {
        processEvent(authority, DbEvent.DeleteElection(name))
    }

    override fun addCandidates(authority: String, electionName: String, candidateNames: List<String>) {
        processEvent(authority, DbEvent.AddCandidates(electionName, candidateNames))
    }

    override fun removeCandidates(authority: String, electionName: String, candidateNames: List<String>) {
        processEvent(authority, DbEvent.RemoveCandidates(electionName, candidateNames))
    }

    override fun castBallot(authority: String, voterName: String, electionName: String, rankings: List<Ranking>) {
        processEvent(authority, DbEvent.CastBallot(voterName, electionName, rankings))
    }

    private fun processEvent(authority: String, dbEvent: DbEvent) {
        eventDbCommands.addEvent(
            authority,
            dbEvent.javaClass.simpleName,
            JsonMappers.compact.writeValueAsString(dbEvent)
        )
    }
}