package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.database.util.GenericDatabase

class StateDbCommandsImpl(genericDatabase: GenericDatabase) : StateDbCommands, GenericDatabase by genericDatabase {
    override fun createUser(name: String, email: String, salt: String, hash: String) {
        update("create-user.sql", name, email, salt, hash)
    }
}
