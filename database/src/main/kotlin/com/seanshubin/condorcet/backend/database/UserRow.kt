package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.domain.Role
import com.seanshubin.condorcet.backend.genericdb.DbRow

data class UserRow(
    val name: String,
    val email: String,
    val salt: String,
    val hash: String,
    val role: Role
) : DbRow<String> {
    override val primaryKey: String = name
    override val cells: List<Any?> = listOf(name, email, salt, hash, role)
}
