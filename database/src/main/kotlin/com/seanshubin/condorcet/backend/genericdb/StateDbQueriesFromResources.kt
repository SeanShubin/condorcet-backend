package com.seanshubin.condorcet.backend.genericdb

import java.sql.ResultSet

class StateDbQueriesFromResources(genericDatabase: GenericDatabase) : StateDbQueries,
    GenericDatabase by genericDatabase {
    override fun findUserByName(name: String): UserRow =
        queryExactlyOneRow(
            ::createUser,
            "user-by-name.sql",
            name
        )

    override fun searchUserByName(name: String): UserRow? =
        queryZeroOrOneRow(
            ::createUser,
            "user-by-name.sql",
            name
        )

    override fun searchUserByEmail(email: String): UserRow? =
        queryZeroOrOneRow(
            ::createUser,
            "user-by-email.sql",
            email
        )


    private fun createUser(resultSet: ResultSet): UserRow {
        val name = resultSet.getString("name")
        val email = resultSet.getString("email")
        val salt = resultSet.getString("salt")
        val hash = resultSet.getString("hash")
        return UserRow(name, email, salt, hash)
    }
}
