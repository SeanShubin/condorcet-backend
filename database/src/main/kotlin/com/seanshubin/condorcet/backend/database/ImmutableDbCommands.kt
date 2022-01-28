package com.seanshubin.condorcet.backend.database

interface ImmutableDbCommands {
    fun addEvent(authority: String, type: String, body: String)
}
