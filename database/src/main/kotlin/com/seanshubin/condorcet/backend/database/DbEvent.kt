package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.domain.Role

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
}
