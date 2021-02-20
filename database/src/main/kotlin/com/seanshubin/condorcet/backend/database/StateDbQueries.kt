package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.domain.Permission
import com.seanshubin.condorcet.backend.domain.Role
import com.seanshubin.condorcet.backend.genericdb.GenericDatabase

interface StateDbQueries : GenericDatabase {
    fun findUserByName(name: String): UserRow
    fun searchUserByName(name: String): UserRow?
    fun searchUserByEmail(email: String): UserRow?
    fun countUsers(): Int
    fun listUsers(): List<UserRow>
    fun roleHasPermission(role: Role, permission: Permission): Boolean
    fun lastSynced(): Int?
    fun searchElectionByName(name: String): ElectionRow?
}
