package com.seanshubin.condorcet.backend.genericdb

import com.seanshubin.condorcet.backend.domain.Role

interface StateDbCommands {
    fun createUser(
        name: String,
        email: String,
        salt: String,
        hash: String,
        role: Role
    )
}
