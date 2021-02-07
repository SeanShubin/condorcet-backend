package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.domain.Permission
import com.seanshubin.condorcet.backend.domain.Role

interface StateDbQueries {
    fun findUserByName(name: String): UserRow
    fun searchUserByName(name: String): UserRow?
    fun searchUserByEmail(email: String): UserRow?
    fun countUsers(): Int
    fun roleHasPermission(role: Role, permission: Permission): Boolean
}
