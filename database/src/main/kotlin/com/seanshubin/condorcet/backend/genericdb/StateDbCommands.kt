package com.seanshubin.condorcet.backend.genericdb

interface StateDbCommands {
    fun createUser(
        name: String,
        email: String,
        salt: String,
        hash: String
    )
}
