package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.domain.Role

data class UserRow(
    val name: String,
    val email: String,
    val salt: String,
    val hash: String,
    val role: Role
)
