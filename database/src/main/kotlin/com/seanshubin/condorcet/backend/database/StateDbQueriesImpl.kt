package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.domain.Permission
import com.seanshubin.condorcet.backend.domain.Role
import com.seanshubin.condorcet.backend.genericdb.GenericDatabase
import java.sql.ResultSet

class StateDbQueriesImpl(genericDatabase: GenericDatabase) : StateDbQueries,
    GenericDatabase by genericDatabase {
    override fun findUserByName(name: String): UserRow =
        queryExactlyOneRow(
            ::createUser,
            "user-by-name",
            name
        )

    override fun searchUserByName(name: String): UserRow? =
        queryZeroOrOneRow(
            ::createUser,
            "user-by-name",
            name
        )

    override fun searchUserByEmail(email: String): UserRow? =
        queryZeroOrOneRow(
            ::createUser,
            "user-by-email",
            email
        )

    override fun countUsers(): Int =
        queryExactlyOneInt("count-users")

    override fun listUsers(): List<UserRow> =
        query(::createUser, "list-users")

    override fun roleHasPermission(role: Role, permission: Permission): Boolean {
        return queryExists("role-has-permission", role.name, permission.name)
    }

    private fun createUser(resultSet: ResultSet): UserRow {
        val name = resultSet.getString("name")
        val email = resultSet.getString("email")
        val salt = resultSet.getString("salt")
        val hash = resultSet.getString("hash")
        val role = Role.valueOf(resultSet.getString("role"))
        return UserRow(name, email, salt, hash, role)
    }
}
