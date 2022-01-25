package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.domain.ElectionUpdates
import com.seanshubin.condorcet.backend.domain.Ranking
import com.seanshubin.condorcet.backend.domain.Role
import java.time.Instant

interface DbEvent {
    fun exec(authority: String, stateDbCommands: StateDbCommands)
    data class AddUser(val name: String, val email: String, val salt: String, val hash: String, val role: Role) :
        DbEvent {
        override fun exec(authority: String, stateDbCommands: StateDbCommands) {
            stateDbCommands.createUser(authority, name, email, salt, hash, role)
        }
    }

    data class SetRole(val name: String, val role: Role) : DbEvent {
        override fun exec(authority: String, stateDbCommands: StateDbCommands) {
            stateDbCommands.setRole(authority, name, role)
        }
    }

    data class RemoveUser(val name: String) : DbEvent {
        override fun exec(authority: String, stateDbCommands: StateDbCommands) {
            stateDbCommands.removeUser(authority, name)
        }
    }

    data class AddElection(val owner: String, val name: String) :
        DbEvent {
        override fun exec(authority: String, stateDbCommands: StateDbCommands) {
            stateDbCommands.addElection(authority, owner, name)
        }
    }

    data class UpdateElection(val name: String, val updates: ElectionUpdates) :
        DbEvent {
        override fun exec(authority: String, stateDbCommands: StateDbCommands) {
            stateDbCommands.updateElection(authority, name, updates)
        }
    }

    data class DeleteElection(val name: String) :
        DbEvent {
        override fun exec(authority: String, stateDbCommands: StateDbCommands) {
            stateDbCommands.deleteElection(authority, name)
        }
    }

    data class AddCandidates(val electionName: String, val candidateNames: List<String>) : DbEvent {
        override fun exec(authority: String, stateDbCommands: StateDbCommands) {
            stateDbCommands.addCandidates(authority, electionName, candidateNames)
        }
    }

    data class RemoveCandidates(val electionName: String, val candidateNames: List<String>) : DbEvent {
        override fun exec(authority: String, stateDbCommands: StateDbCommands) {
            stateDbCommands.removeCandidates(authority, electionName, candidateNames)
        }
    }

    data class AddVoters(val electionName: String, val voterNames: List<String>) : DbEvent {
        override fun exec(authority: String, stateDbCommands: StateDbCommands) {
            stateDbCommands.addVoters(authority, electionName, voterNames)
        }
    }

    data class RemoveVoters(val electionName: String, val voterNames: List<String>) : DbEvent {
        override fun exec(authority: String, stateDbCommands: StateDbCommands) {
            stateDbCommands.removeVoters(authority, electionName, voterNames)
        }
    }

    data class CastBallot(
        val voterName: String,
        val electionName: String,
        val rankings: List<Ranking>,
        val confirmation: String,
        val now: Instant
    ) : DbEvent {
        override fun exec(authority: String, stateDbCommands: StateDbCommands) {
            stateDbCommands.castBallot(authority, voterName, electionName, rankings, confirmation, now)
        }
    }

    data class UpdateWhenCast(val confirmation: String, val now:Instant) : DbEvent {
        override fun exec(authority: String, stateDbCommands: StateDbCommands) {
            stateDbCommands.updateWhenCast(authority, confirmation, now)
        }
    }

    data class SetRankings(val confirmation: String, val electionName:String, val rankings: List<Ranking>) : DbEvent {
        override fun exec(authority: String, stateDbCommands: StateDbCommands) {
            stateDbCommands.setRankings(authority, confirmation, electionName, rankings)
        }
    }
}
