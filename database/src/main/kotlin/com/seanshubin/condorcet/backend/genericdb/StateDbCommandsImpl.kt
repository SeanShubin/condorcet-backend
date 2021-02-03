package com.seanshubin.condorcet.backend.genericdb

class StateDbCommandsImpl(genericDatabase: GenericDatabase) : StateDbCommands, GenericDatabase by genericDatabase {
    override fun createUser(name: String, email: String, salt: String, hash: String) {
        update("create-user", name, email, salt, hash)
    }
}
