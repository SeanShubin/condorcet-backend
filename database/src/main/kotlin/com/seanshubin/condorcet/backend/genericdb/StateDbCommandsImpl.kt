package com.seanshubin.condorcet.backend.genericdb

import com.seanshubin.condorcet.backend.domain.Role

class StateDbCommandsImpl(genericDatabase: GenericDatabase) : StateDbCommands, GenericDatabase by genericDatabase {
    override fun createUser(name: String, email: String, salt: String, hash: String, role: Role) {
        update("create-user", name, email, salt, hash, role.name)
    }
}
