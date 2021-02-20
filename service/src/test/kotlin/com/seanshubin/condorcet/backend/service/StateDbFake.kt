package com.seanshubin.condorcet.backend.service

import com.seanshubin.condorcet.backend.database.StateDbCommands
import com.seanshubin.condorcet.backend.database.StateDbQueries
import com.seanshubin.condorcet.backend.database.UserRow
import com.seanshubin.condorcet.backend.domain.Permission
import com.seanshubin.condorcet.backend.domain.Role
import com.seanshubin.condorcet.backend.genericdb.GenericTable
import com.seanshubin.condorcet.backend.genericdb.Schema
import java.sql.ResultSet

class StateDbFake : StateDbQueries, StateDbCommands {
    val userRows = mutableListOf<UserRow>()
    override fun setLastSynced(lastSynced: Int) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun initializeLastSynced(lastSynced: Int) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun createUser(authority: String, name: String, email: String, salt: String, hash: String, role: Role) {
        userRows.add(UserRow(name, email, salt, hash, role))
    }

    override fun setRole(authority: String, name: String, role: Role) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun removeUser(authority: String, name: String) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun findUserByName(name: String): UserRow =
        searchUserByName(name)!!

    override fun searchUserByName(name: String): UserRow? =
        userRows.find { userRow -> userRow.name == name }

    override fun searchUserByEmail(email: String): UserRow? =
        userRows.find { userRow -> userRow.email == email }

    override fun countUsers(): Int = userRows.size

    override fun listUsers(): List<UserRow> {
        throw UnsupportedOperationException("not implemented")
    }

    override fun roleHasPermission(role: Role, permission: Permission): Boolean {
        throw UnsupportedOperationException("not implemented")
    }

    override fun lastSynced(): Int? {
        throw UnsupportedOperationException("not implemented")
    }

    override fun <T> queryExactlyOneRow(
        createFunction: (ResultSet) -> T,
        queryPath: String,
        vararg parameters: Any?
    ): T {
        throw UnsupportedOperationException("not implemented")
    }

    override fun <T> queryZeroOrOneRow(
        createFunction: (ResultSet) -> T,
        queryPath: String,
        vararg parameters: Any?
    ): T? {
        throw UnsupportedOperationException("not implemented")
    }

    override fun queryExists(queryPath: String, vararg parameters: Any?): Boolean {
        throw UnsupportedOperationException("not implemented")
    }

    override fun <T> query(createFunction: (ResultSet) -> T, queryPath: String, vararg parameters: Any?): List<T> {
        throw UnsupportedOperationException("not implemented")
    }

    override fun queryUntyped(query: String, vararg parameters: Any?): GenericTable {
        throw UnsupportedOperationException("not implemented")
    }

    override fun queryExactlyOneInt(queryPath: String, vararg parameters: Any?): Int {
        throw UnsupportedOperationException("not implemented")
    }

    override fun queryZeroOrOneInt(queryPath: String, vararg parameters: Any?): Int? {
        throw UnsupportedOperationException("not implemented")
    }

    override fun update(queryPath: String, vararg parameters: Any?): Int {
        throw UnsupportedOperationException("not implemented")
    }

    override fun tableNames(schema: Schema): List<String> {
        throw UnsupportedOperationException("not implemented")
    }

    override fun tableData(schema: Schema, name: String): GenericTable {
        throw UnsupportedOperationException("not implemented")
    }
}