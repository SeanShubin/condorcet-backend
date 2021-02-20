package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.domain.Permission
import com.seanshubin.condorcet.backend.domain.Role
import com.seanshubin.condorcet.backend.genericdb.GenericDatabase
import java.sql.ResultSet
import java.time.Instant

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

    override fun lastSynced(): Int? = queryZeroOrOneInt("get-last-synced")

    override fun searchElectionByName(name: String): ElectionRow? {
        return queryZeroOrOneRow(::createElection, "election-by-name", name)
    }

    private fun createUser(resultSet: ResultSet): UserRow {
        val name = resultSet.getString("name")
        val email = resultSet.getString("email")
        val salt = resultSet.getString("salt")
        val hash = resultSet.getString("hash")
        val role = Role.valueOf(resultSet.getString("role"))
        return UserRow(name, email, salt, hash, role)
    }

    private fun createElection(resultSet: ResultSet): ElectionRow {
        val owner = resultSet.getString("owner")
        val name: String = resultSet.getString("name")
        val start: Instant? = resultSet.getTimestamp("start").toInstant()
        val end: Instant? = resultSet.getTimestamp("end").toInstant()
        val secret: Boolean = resultSet.getBoolean("secret")
        val doneConfiguring: Instant? = resultSet.getTimestamp("done_configuring").toInstant()
        val template: Boolean = resultSet.getBoolean("template")
        val started: Boolean = resultSet.getBoolean("started")
        val finished: Boolean = resultSet.getBoolean("finished")
        val canChangeCandidatesAfterDoneConfiguring: Boolean =
            resultSet.getBoolean("can_change_candidates_after_done_configuring")
        val ownerCanDeleteBallots: Boolean = resultSet.getBoolean("owner_can_delete_ballots")
        val auditorCanDeleteBallots: Boolean = resultSet.getBoolean("auditor_can_delete_ballots")
        return ElectionRow(
            owner, name, start, end, secret, doneConfiguring, template, started, finished,
            canChangeCandidatesAfterDoneConfiguring, ownerCanDeleteBallots, auditorCanDeleteBallots
        )
    }
}
