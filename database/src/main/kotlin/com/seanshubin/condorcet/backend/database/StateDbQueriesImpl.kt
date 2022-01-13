package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.domain.Ballot
import com.seanshubin.condorcet.backend.domain.Permission
import com.seanshubin.condorcet.backend.domain.Ranking
import com.seanshubin.condorcet.backend.domain.Role
import com.seanshubin.condorcet.backend.genericdb.GenericDatabase
import java.sql.ResultSet
import java.time.Instant

class StateDbQueriesImpl(genericDatabase: GenericDatabase) : StateDbQueries,
    GenericDatabase by genericDatabase {
    override fun findUserByName(name: String): UserRow =
        queryExactlyOneRow(
            ::createUser,
            "user-select-by-name",
            name
        )

    override fun searchUserByName(name: String): UserRow? =
        queryZeroOrOneRow(
            ::createUser,
            "user-select-by-name",
            name
        )

    override fun searchUserByEmail(email: String): UserRow? =
        queryZeroOrOneRow(
            ::createUser,
            "user-select-by-email",
            email
        )

    override fun tableCount(): Int = StateSchema.tables.size

    override fun userCount(): Int =
        queryExactlyOneInt("user-count")

    override fun electionCount(): Int =
        queryExactlyOneInt("election-count")

    override fun candidateCount(electionName: String): Int =
        queryExactlyOneInt("candidate-count-by-election", electionName)

    override fun voterCount(electionName: String): Int =
        queryExactlyOneInt("voter-count-by-election", electionName)

    override fun listUsers(): List<UserRow> =
        query(::createUser, "user-select")

    override fun listElections(): List<ElectionRow> =
        query(::createElection, "election-select")

    override fun roleHasPermission(role: Role, permission: Permission): Boolean {
        return queryExists("role-permission-exists", role.name, permission.name)
    }

    override fun lastSynced(): Int? = queryZeroOrOneInt("variable-select-last-synced")

    override fun searchElectionByName(name: String): ElectionRow? {
        return queryZeroOrOneRow(::createElection, "election-select-by-name", name)
    }

    override fun listCandidates(electionName: String): List<String> =
        query(::createName, "candidate-select-by-election", electionName)

    override fun listRankings(voterName: String, electionName: String): List<Ranking> =
        query(::createRanking, "ranking-select-by-user-election", voterName, electionName)

    override fun searchBallot(voterName: String, electionName: String): BallotRow? {
        return queryZeroOrOneRow(this::createBallot, "ballot-select-by-user-election", voterName, electionName)
    }

    override fun listRankings(electionName: String): List<VoterElectionRankingRow> =
        query(::createVoterElectionRankingRow, "ranking-by-election", electionName)

    override fun listBallots(electionName: String): List<Ballot> =
        queryJoin(
            ::createBallotRow,
            ::createRankingRow,
            ::createBallotRankingKey,
            ::createBallot,
            "ranking-select-by-election",
            electionName
        )

    override fun listVoterNames(): List<String> =
        query(::createName,"user-by-permission", Permission.USE_APPLICATION.toString())

    override fun listVotersForElection(electionName: String): List<String> =
        query(::createName, "voter-by-election", electionName)

    override fun listUserNames(): List<String> =
        query(::createName, "user-select")

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
        val noVotingBefore: Instant? = resultSet.getTimestamp("no_voting_before")?.toInstant()
        val noVotingAfter: Instant? = resultSet.getTimestamp("no_voting_after")?.toInstant()
        val allowEdit: Boolean = resultSet.getBoolean("allow_edit")
        val allowVote: Boolean = resultSet.getBoolean("allow_vote")
        return ElectionRow(
            owner, name, secretBallot, noVotingBefore, noVotingAfter, allowEdit, allowVote
        )
    }

    private fun createBallot(resultSet: ResultSet): BallotRow {
        val userName:String = resultSet.getString("user_name")
        val electionName:String = resultSet.getString("election_name")
        val confirmation:String = resultSet.getString("confirmation")
        val whenCast: Instant = resultSet.getTimestamp("when_cast").toInstant()
        return BallotRow(userName, electionName, confirmation, whenCast)
    }

    private fun createName(resultSet: ResultSet): String =
        resultSet.getString("name")

    private fun createRanking(resultSet: ResultSet): Ranking =
        Ranking(
            resultSet.getString("candidate"),
            resultSet.getInt("rank")
        )

    private fun createVoterElectionRankingRow(resultSet: ResultSet): VoterElectionRankingRow =
        VoterElectionRankingRow(
            resultSet.getString("voter"),
            resultSet.getString("election"),
            resultSet.getString("candidate"),
            resultSet.getInt("rank")
        )

    private fun createBallotRow(resultSet: ResultSet): BallotRow =
        BallotRow(
            resultSet.getString("user"),
            resultSet.getString("election"),
            resultSet.getString("confirmation"),
            resultSet.getTimestamp("when_cast").toInstant()
        )

    private fun createRankingRow(resultSet: ResultSet): RankingRow =
        RankingRow(
            resultSet.getString("candidate"),
            resultSet.getInt("rank")
        )

    private fun createBallotRankingKey(resultSet: ResultSet): Int =
        resultSet.getInt("ballot.id")

    private fun createBallot(ballot: BallotRow, rankingList: List<RankingRow>): Ballot =
        Ballot(
            ballot.user,
            ballot.election,
            ballot.confirmation,
            ballot.whenCast,
            rankingList.map { Ranking(it.candidate, it.rank) })
}
