package com.seanshubin.condorcet.backend.genericdb

import com.seanshubin.condorcet.backend.domain.Role
import com.seanshubin.condorcet.backend.json.JsonMappers

class SyncDbCommands(private val eventDbCommands: EventDbCommands) : StateDbCommands {
    override fun createUser(name: String, email: String, salt: String, hash: String, role: Role) {
        processEvent(DbEvent.AddUser(name, email, salt, hash, role))
    }

    private fun processEvent(dbEvent: DbEvent) {
        eventDbCommands.addEvent(dbEvent.javaClass.simpleName, JsonMappers.compact.writeValueAsString(dbEvent))
    }
}