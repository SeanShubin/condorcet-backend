package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.domain.ElectionUpdates
import com.seanshubin.condorcet.backend.domain.Ranking
import com.seanshubin.condorcet.backend.domain.Role
import com.seanshubin.condorcet.backend.json.JsonMappers
import java.time.Clock
import java.time.Instant

class SyncCommands(
    private val immutableDbCommands: ImmutableDbCommands,
    private val clock: Clock
) : MutableDbCommands {
    override fun setLastSynced(lastSynced: Int) {
        // do not sync the commands used to sync the commands
    }

    override fun initializeLastSynced(lastSynced: Int) {
        // do not sync the commands used to sync the commands
    }

    override fun createUser(authority: String, userName: String, email: String, salt: String, hash: String, role: Role) {
        processEvent(authority, EventCommand.AddUser(userName, email, salt, hash, role))
    }

    override fun setRole(authority: String, userName: String, role: Role) {
        processEvent(authority, EventCommand.SetRole(userName, role))
    }

    override fun removeUser(authority: String, userName: String) {
        processEvent(authority, EventCommand.RemoveUser(userName))
    }

    override fun addElection(authority: String, owner: String, electionName: String) {
        processEvent(authority, EventCommand.AddElection(owner, electionName))
    }

    override fun updateElection(authority: String, electionName: String, updates: ElectionUpdates) {
        processEvent(authority, EventCommand.UpdateElection(electionName, updates))
    }

    override fun deleteElection(authority: String, electionName: String) {
        processEvent(authority, EventCommand.DeleteElection(electionName))
    }

    override fun addCandidates(authority: String, electionName: String, candidateNames: List<String>) {
        processEvent(authority, EventCommand.AddCandidates(electionName, candidateNames))
    }

    override fun removeCandidates(authority: String, electionName: String, candidateNames: List<String>) {
        processEvent(authority, EventCommand.RemoveCandidates(electionName, candidateNames))
    }

    override fun addVoters(authority: String, electionName: String, voterNames: List<String>) {
        processEvent(authority, EventCommand.AddVoters(electionName, voterNames))
    }

    override fun removeVoters(authority: String, electionName: String, voterNames: List<String>) {
        processEvent(authority, EventCommand.RemoveVoters(electionName, voterNames))
    }

    override fun castBallot(authority: String, voterName: String, electionName: String, rankings: List<Ranking>, confirmation:String, now:Instant) {
        processEvent(authority, EventCommand.CastBallot(voterName, electionName, rankings, confirmation, now))
    }

    override fun updateWhenCast(authority: String, confirmation: String, now: Instant) {
        processEvent(authority, EventCommand.UpdateWhenCast(confirmation, now))
    }

    override fun setRankings(authority: String, confirmation: String, electionName: String, rankings: List<Ranking>) {
        processEvent(authority, EventCommand.SetRankings(confirmation, electionName, rankings))
    }

    override fun setPassword(authority: String, userName: String, salt: String, hash:String) {
        processEvent(authority, EventCommand.SetPassword(userName, salt, hash))
    }

    private fun processEvent(authority: String, eventCommand: EventCommand) {
        val now = clock.instant()
        immutableDbCommands.addEvent(
            authority,
            eventCommand.javaClass.simpleName,
            JsonMappers.compact.writeValueAsString(eventCommand),
            now
        )
    }
}