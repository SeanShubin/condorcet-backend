package com.seanshubin.condorcet.backend.service

import com.seanshubin.condorcet.backend.database.UserRow
import com.seanshubin.condorcet.backend.domain.Role

class StateFake : StateNotImplemented {
    val userRows = mutableListOf<UserRow>()
    override fun createUser(authority: String, name: String, email: String, salt: String, hash: String, role: Role) {
        userRows.add(UserRow(name, email, salt, hash, role))
    }

    override fun findUserByName(name: String): UserRow =
        searchUserByName(name)!!

    override fun searchUserByName(name: String): UserRow? =
        userRows.find { userRow -> userRow.name == name }

    override fun searchUserByEmail(email: String): UserRow? =
        userRows.find { userRow -> userRow.email == email }

    override fun userCount(): Int = userRows.size
}
