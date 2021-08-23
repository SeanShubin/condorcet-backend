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

    override fun tableCount(): Int = StateSchema.tables.size

    override fun userCount(): Int =
        queryExactlyOneInt("count-users")

    override fun electionCount(): Int =
        queryExactlyOneInt("count-elections")

    override fun listUsers(): List<UserRow> =
        query(::createUser, "list-users")

    override fun listElections(): List<ElectionRow> =
        query(::createElection, "list-elections")

    override fun roleHasPermission(role: Role, permission: Permission): Boolean {
        return queryExists("role-has-permission", role.name, permission.name)
    }

    override fun lastSynced(): Int? = queryZeroOrOneInt("get-last-synced")

    override fun searchElectionByName(name: String): ElectionRow? {
        return queryZeroOrOneRow(::createElection, "election-by-name", name)
    }

    override fun listCandidates(electionName: String): List<String> =
        query(::createCandidateName, "candidate-names-by-election", electionName)

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
        val secretBallot: Boolean = resultSet.getBoolean("secret_ballot")
        val scheduledStart: Instant? = resultSet.getTimestamp("scheduled_start")?.toInstant()
        val scheduledEnd: Instant? = resultSet.getTimestamp("scheduled_end")?.toInstant()
        val restrictWhoCanVote: Boolean = resultSet.getBoolean("restrict_who_can_vote")
        val ownerCanDeleteBallots: Boolean = resultSet.getBoolean("owner_can_delete_ballots")
        val auditorCanDeleteBallots: Boolean = resultSet.getBoolean("auditor_can_delete_ballots")
        val isTemplate: Boolean = resultSet.getBoolean("is_template")
        val noChangesAfterVote: Boolean = resultSet.getBoolean("no_changes_after_vote")
        val isOpen: Boolean = resultSet.getBoolean("is_open")
        val candidateCount: Int = resultSet.getInt("candidate_count")
        return ElectionRow(
            owner, name, secretBallot, scheduledStart, scheduledEnd, restrictWhoCanVote,
            ownerCanDeleteBallots, auditorCanDeleteBallots, isTemplate, noChangesAfterVote, isOpen,
            candidateCount
        )
    }

    private fun createCandidateName(resultSet: ResultSet): String =
        resultSet.getString("name")
}
