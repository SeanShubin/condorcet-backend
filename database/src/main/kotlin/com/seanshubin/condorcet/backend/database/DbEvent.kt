package com.seanshubin.condorcet.backend.database

interface DbEvent {
    fun exec(stateDbCommands: StateDbCommands)
    data class AddUser(val name: String, val email: String, val salt: String, val hash: String) : DbEvent {
        override fun exec(stateDbCommands: StateDbCommands) {
            stateDbCommands.createUser(name, email, salt, hash)
        }
    }
}
