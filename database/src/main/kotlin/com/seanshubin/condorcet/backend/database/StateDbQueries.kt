package com.seanshubin.condorcet.backend.database

interface StateDbQueries {
    fun findUserByName(name: String): UserRow
    fun searchUserByName(name: String): UserRow?
    fun searchUserByEmail(email: String): UserRow?
}
