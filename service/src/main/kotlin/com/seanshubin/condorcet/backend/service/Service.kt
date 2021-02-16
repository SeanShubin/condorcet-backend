package com.seanshubin.condorcet.backend.service

import com.seanshubin.condorcet.backend.domain.Role
import com.seanshubin.condorcet.backend.domain.TableData
import com.seanshubin.condorcet.backend.domain.UserNameRole

interface Service {
    fun refresh(refreshToken: RefreshToken): Tokens
    fun register(rawName: String, email: String, password: String): Tokens
    fun authenticate(nameOrEmail: String, password: String): Tokens
    fun setRole(accessToken: AccessToken, name: String, role: Role)
    fun removeUser(accessToken: AccessToken, name: String)
    fun listUsers(accessToken: AccessToken): List<UserNameRole>
    fun listTables(accessToken: AccessToken): List<String>
    fun tableData(accessToken: AccessToken, name: String): TableData
}
