package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.domain.Role
import com.seanshubin.condorcet.backend.genericdb.GenericDatabase

class StateDbCommandsImpl(genericDatabase: GenericDatabase) : StateDbCommands, GenericDatabase by genericDatabase {
    override fun setLastSynced(lastSynced: Int) {
        update("set-last-synced", lastSynced)
    }

    override fun initializeLastSynced(lastSynced: Int) {
        update("initialize-last-synced", lastSynced)
    }

    override fun createUser(authority: String, name: String, email: String, salt: String, hash: String, role: Role) {
        update("create-user", name, email, salt, hash, role.name)
    }

    override fun setRole(authority: String, name: String, role: Role) {
        update("set-role", role.name, name)
    }

    override fun removeUser(authority: String, name: String) {
        update("delete-user", name)
    }

    override fun addElection(authority: String, owner: String, name: String) {
        update("add-election", owner, name)
    }
}
