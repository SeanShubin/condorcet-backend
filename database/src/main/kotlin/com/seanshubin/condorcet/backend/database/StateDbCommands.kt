package com.seanshubin.condorcet.backend.database

interface StateDbCommands {
    fun createUser(
        name: String,
        email: String,
        salt: String,
        hash: String
    )
}
