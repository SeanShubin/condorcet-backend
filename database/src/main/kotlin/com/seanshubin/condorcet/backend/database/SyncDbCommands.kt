package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.json.JsonMappers

class SyncDbCommands(private val eventDbCommands: EventDbCommands) : StateDbCommands {
    override fun createUser(name: String, email: String, salt: String, hash: String) {
        processEvent(DbEvent.AddUser(name, email, salt, hash))
    }

    private fun processEvent(dbEvent: DbEvent) {
        eventDbCommands.addEvent(dbEvent.javaClass.simpleName, JsonMappers.compact.writeValueAsString(dbEvent))
    }
}