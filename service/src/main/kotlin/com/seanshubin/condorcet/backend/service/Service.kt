package com.seanshubin.condorcet.backend.service

import com.seanshubin.condorcet.backend.domain.Election
import com.seanshubin.condorcet.backend.domain.Role
import com.seanshubin.condorcet.backend.domain.TableData
import com.seanshubin.condorcet.backend.domain.UserNameRole

interface Service {
    fun synchronize()
    fun refresh(refreshToken: RefreshToken): Tokens
    fun register(rawName: String, email: String, password: String): Tokens
    fun authenticate(nameOrEmail: String, password: String): Tokens
    fun setRole(accessToken: AccessToken, name: String, role: Role)
    fun removeUser(accessToken: AccessToken, name: String)
    fun listUsers(accessToken: AccessToken): List<UserNameRole>
    fun addElection(accessToken: AccessToken, name: String)
    fun listElections(accessToken: AccessToken): List<Election>
    fun listTables(accessToken: AccessToken): List<String>
    fun userCount(accessToken: AccessToken): Int
    fun electionCount(accessToken: AccessToken): Int
    fun tableCount(accessToken: AccessToken): Int
    fun eventCount(accessToken: AccessToken): Int
    fun tableData(accessToken: AccessToken, name: String): TableData
    fun eventData(accessToken: AccessToken): TableData
}
