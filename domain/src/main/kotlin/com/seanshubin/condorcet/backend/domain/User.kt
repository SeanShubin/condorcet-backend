package com.seanshubin.condorcet.backend.domain

data class User(
    val name: String,
    val email: String,
    val salt: String,
    val hash: String,
    val role: Role
)
