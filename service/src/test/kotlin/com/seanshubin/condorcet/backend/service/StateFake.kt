package com.seanshubin.condorcet.backend.service

import com.seanshubin.condorcet.backend.domain.User
import com.seanshubin.condorcet.backend.domain.Role

class StateFake : StateNotImplemented {
    val users = mutableListOf<User>()
    override fun createUser(authority: String, name: String, email: String, salt: String, hash: String, role: Role) {
        users.add(User(name, email, salt, hash, role))
    }

    override fun findUserByName(name: String): User =
        searchUserByName(name)!!

    override fun searchUserByName(name: String): User? =
        users.find { userRow -> userRow.name == name }

    override fun searchUserByEmail(email: String): User? =
        users.find { userRow -> userRow.email == email }

    override fun userCount(): Int = users.size
}