package com.seanshubin.condorcet.backend.service

import com.seanshubin.condorcet.backend.domain.Role
import com.seanshubin.condorcet.backend.domain.UserNameRole

interface Service {
    fun register(name: String, email: String, password: String): Tokens
    fun authenticate(nameOrEmail: String, password: String): Tokens
    fun setRole(authority: String, target: String, role: Role)
    fun removeUser(authority: String, target: String)
    fun listUsers(authority: String): List<UserNameRole>
}
