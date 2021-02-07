package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.domain.Role

interface DbEvent {
    fun exec(stateDbCommands: StateDbCommands)
    data class AddUser(val name: String, val email: String, val salt: String, val hash: String, val role: Role) :
        DbEvent {
        override fun exec(stateDbCommands: StateDbCommands) {
            stateDbCommands.createUser(name, email, salt, hash, role)
        }
    }

    data class SetRole(val name: String, val role: Role) : DbEvent {
        override fun exec(stateDbCommands: StateDbCommands) {
            stateDbCommands.setRole(name, role)
        }
    }
}
