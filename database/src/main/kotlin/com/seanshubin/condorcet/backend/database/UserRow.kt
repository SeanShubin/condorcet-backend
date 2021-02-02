package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.database.util.DbRow

class UserRow(
    val name: String,
    val email: String,
    val salt: String,
    val hash: String
) : DbRow<String> {
    override val primaryKey: String = name
    override val cells: List<Any?> = listOf(name, email, salt, hash)
}
