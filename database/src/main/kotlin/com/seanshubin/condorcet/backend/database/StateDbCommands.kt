package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.domain.Role

interface StateDbCommands {
    fun createUser(
        name: String,
        email: String,
        salt: String,
        hash: String,
        role: Role
    )

    fun setRole(name: String, role: Role)
}
