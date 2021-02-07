package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.domain.Role
import com.seanshubin.condorcet.backend.json.JsonMappers

class SyncDbCommands(private val eventDbCommands: EventDbCommands) : StateDbCommands {
    override fun createUser(name: String, email: String, salt: String, hash: String, role: Role) {
        processEvent(DbEvent.AddUser(name, email, salt, hash, role))
    }

    override fun setRole(name: String, role: Role) {
        processEvent(DbEvent.SetRole(name, role))
    }

    override fun removeUser(name: String) {
        processEvent(DbEvent.RemoveUser(name))
    }

    private fun processEvent(dbEvent: DbEvent) {
        eventDbCommands.addEvent(dbEvent.javaClass.simpleName, JsonMappers.compact.writeValueAsString(dbEvent))
    }
}