package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.domain.Role

interface StateDbCommands {
    fun createUser(
        authority: String,
        name: String,
        email: String,
        salt: String,
        hash: String,
        role: Role
    )

    fun setRole(authority: String, name: String, role: Role)

    fun removeUser(authority: String, name: String)
}
