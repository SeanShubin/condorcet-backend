package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.domain.*
import com.seanshubin.condorcet.backend.genericdb.GenericDatabase
import java.sql.ResultSet
import java.time.Instant

class MutableDbQueriesImpl(genericDatabase: GenericDatabase) : MutableDbQueries,
    GenericDatabase by genericDatabase {
    override fun findUserByName(name: String): User =
        queryExactlyOneRow(
            ::createUser,
            "user-select-by-name",
            name
        )

    override fun findUserByEmail(email: String): User =
        queryExactlyOneRow(
            ::createUser,
            "user-select-by-email",
            email
        )

    override fun searchUserByName(name: String): User? =
        queryZeroOrOneRow(
            ::createUser,
            "user-select-by-name",
            name
        )

    override fun searchUserByEmail(email: String): User? =
        queryZeroOrOneRow(
            ::createUser,
            "user-select-by-email",
            email
        )

    override fun tableCount(): Int = MutableDbSchema.tables.size

    override fun userCount(): Int =
        queryExactlyOneInt("user-count")

    override fun searchElectionByName(name: String): ElectionSummary? =
        queryZeroOrOneRow(::createElectionSummary, "election-select-by-name", name)

    override fun electionCount(): Int =
        queryExactlyOneInt("election-count")

    override fun candidateCount(electionName: String): Int =
        queryExactlyOneInt("candidate-count-by-election", electionName)

    override fun voterCount(electionName: String): Int =
        queryExactlyOneInt("voter-count-by-election", electionName)

    override fun listUsers(): List<User> =
        query(::createUser, "user-select")

    override fun listElections(): List<ElectionSummary> =
        query(::createElectionSummary, "election-select")

    override fun roleHasPermission(role: Role, permission: Permission): Boolean {
        return queryExists("role-permission-exists", role.name, permission.name)
    }

    override fun lastSynced(): Int? = queryZeroOrOneInt("variable-select-last-synced")

    override fun listCandidates(electionName: String): List<String> =
        query(::createName, "candidate-select-by-election", electionName)

    override fun listRankings(voterName: String, electionName: String): List<Ranking> =
        query(::createRanking, "ranking-select-by-user-election", voterName, electionName)

    override fun searchBallot(voterName: String, electionName: String): BallotSummary? {
        return queryZeroOrOneRow(
            this::attachRankingsToBallot,
            "ballot-select-by-user-election",
            voterName,
            electionName
        )
    }

    override fun listRankings(electionName: String): List<VoterElectionCandidateRank> =
        query(::createVoterElectionRankingRow, "ranking-by-election", electionName)

    override fun listBallots(electionName: String): List<RevealedBallot> =
        queryParentChild(
            ::createBallotSummary,
            ::createRanking,
            ::createBallotKey,
            ::attachRankingsToBallot,
            "ranking-select-by-election",
            electionName
        )

    override fun listVoterNames(): List<String> =
        query(::createName, "user-by-permission", Permission.USE_APPLICATION.toString())

    override fun listVotersForElection(electionName: String): List<String> =
        query(::createName, "user-by-election", electionName)

    override fun listUserNames(): List<String> =
        query(::createName, "user-select")

    override fun listPermissions(role: Role): List<Permission> =
        query(::createPermission, "role-permission-select-by-role", role.toString())

    private fun createUser(resultSet: ResultSet): User {
        val name = resultSet.getString("name")
        val email = resultSet.getString("email")
        val salt = resultSet.getString("salt")
        val hash = resultSet.getString("hash")
        val role = Role.valueOf(resultSet.getString("role"))
        return User(name, email, salt, hash, role)
    }

    private fun createElectionSummary(resultSet: ResultSet): ElectionSummary {
        val owner = resultSet.getString("owner")
        val name: String = resultSet.getString("name")
        val secretBallot: Boolean = resultSet.getBoolean("secret_ballot")
        val noVotingBefore: Instant? = resultSet.getTimestamp("no_voting_before")?.toInstant()
        val noVotingAfter: Instant? = resultSet.getTimestamp("no_voting_after")?.toInstant()
        val allowEdit: Boolean = resultSet.getBoolean("allow_edit")
        val allowVote: Boolean = resultSet.getBoolean("allow_vote")
        return ElectionSummary(
            owner, name, secretBallot, noVotingBefore, noVotingAfter, allowEdit, allowVote
        )
    }

    private fun attachRankingsToBallot(resultSet: ResultSet): BallotSummary {
        val userName: String = resultSet.getString("user_name")
        val electionName: String = resultSet.getString("election_name")
        val confirmation: String = resultSet.getString("confirmation")
        val whenCast: Instant = resultSet.getTimestamp("when_cast").toInstant()
        return BallotSummary(userName, electionName, confirmation, whenCast)
    }

    private fun createName(resultSet: ResultSet): String =
        resultSet.getString("name")

    private fun createPermission(resultSet: ResultSet): Permission =
        Permission.valueOf(resultSet.getString("permission"))

    private fun createRanking(resultSet: ResultSet): Ranking =
        Ranking(
            resultSet.getString("candidate"),
            resultSet.getInt("rank")
        )

    private fun createVoterElectionRankingRow(resultSet: ResultSet): VoterElectionCandidateRank =
        VoterElectionCandidateRank(
            resultSet.getString("voter"),
            resultSet.getString("election"),
            resultSet.getString("candidate"),
            resultSet.getInt("rank")
        )

    private fun createBallotSummary(resultSet: ResultSet): BallotSummary =
        BallotSummary(
            resultSet.getString("user"),
            resultSet.getString("election"),
            resultSet.getString("confirmation"),
            resultSet.getTimestamp("when_cast").toInstant()
        )

    private fun createBallotKey(resultSet: ResultSet): Int =
        resultSet.getInt("ballot.id")

    private fun attachRankingsToBallot(ballotSummary: BallotSummary, rankingList: List<Ranking>): RevealedBallot =
        RevealedBallot(
            ballotSummary.voterName,
            ballotSummary.electionName,
            ballotSummary.confirmation,
            ballotSummary.whenCast,
            rankingList.map { Ranking(it.candidateName, it.rank) })
}
