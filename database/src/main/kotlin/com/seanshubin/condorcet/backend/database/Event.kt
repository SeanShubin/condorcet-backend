package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.domain.ElectionUpdates
import com.seanshubin.condorcet.backend.domain.Ranking
import com.seanshubin.condorcet.backend.domain.Role
import java.time.Instant

interface Event {
    fun exec(authority: String, stateCommands: StateCommands)
    data class AddUser(val name: String, val email: String, val salt: String, val hash: String, val role: Role) :
        Event {
        override fun exec(authority: String, stateCommands: StateCommands) {
            stateCommands.createUser(authority, name, email, salt, hash, role)
        }
    }

    data class SetRole(val name: String, val role: Role) : Event {
        override fun exec(authority: String, stateCommands: StateCommands) {
            stateCommands.setRole(authority, name, role)
        }
    }

    data class RemoveUser(val name: String) : Event {
        override fun exec(authority: String, stateCommands: StateCommands) {
            stateCommands.removeUser(authority, name)
        }
    }

    data class AddElection(val owner: String, val name: String) :
        Event {
        override fun exec(authority: String, stateCommands: StateCommands) {
            stateCommands.addElection(authority, owner, name)
        }
    }

    data class UpdateElection(val name: String, val updates: ElectionUpdates) :
        Event {
        override fun exec(authority: String, stateCommands: StateCommands) {
            stateCommands.updateElection(authority, name, updates)
        }
    }

    data class DeleteElection(val name: String) :
        Event {
        override fun exec(authority: String, stateCommands: StateCommands) {
            stateCommands.deleteElection(authority, name)
        }
    }

    data class AddCandidates(val electionName: String, val candidateNames: List<String>) : Event {
        override fun exec(authority: String, stateCommands: StateCommands) {
            stateCommands.addCandidates(authority, electionName, candidateNames)
        }
    }

    data class RemoveCandidates(val electionName: String, val candidateNames: List<String>) : Event {
        override fun exec(authority: String, stateCommands: StateCommands) {
            stateCommands.removeCandidates(authority, electionName, candidateNames)
        }
    }

    data class AddVoters(val electionName: String, val voterNames: List<String>) : Event {
        override fun exec(authority: String, stateCommands: StateCommands) {
            stateCommands.addVoters(authority, electionName, voterNames)
        }
    }

    data class RemoveVoters(val electionName: String, val voterNames: List<String>) : Event {
        override fun exec(authority: String, stateCommands: StateCommands) {
            stateCommands.removeVoters(authority, electionName, voterNames)
        }
    }

    data class CastBallot(
        val voterName: String,
        val electionName: String,
        val rankings: List<Ranking>,
        val confirmation: String,
        val now: Instant
    ) : Event {
        override fun exec(authority: String, stateCommands: StateCommands) {
            stateCommands.castBallot(authority, voterName, electionName, rankings, confirmation, now)
        }
    }

    data class UpdateWhenCast(val confirmation: String, val now:Instant) : Event {
        override fun exec(authority: String, stateCommands: StateCommands) {
            stateCommands.updateWhenCast(authority, confirmation, now)
        }
    }

    data class SetRankings(val confirmation: String, val electionName:String, val rankings: List<Ranking>) : Event {
        override fun exec(authority: String, stateCommands: StateCommands) {
            stateCommands.setRankings(authority, confirmation, electionName, rankings)
        }
    }
}
