package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.domain.Permission
import com.seanshubin.condorcet.backend.domain.Role
import com.seanshubin.condorcet.backend.genericdb.*
import com.seanshubin.condorcet.backend.genericdb.FieldType.*

object StateSchema : Schema {
    val intVariableName = Field("name", STRING, unique = true)
    val intVariableValue = Field("value", INT)
    val intVariable = Table("int_variable", intVariableName, intVariableValue)
    val rolePermissionRole = DbEnum("role", Role.values().toList())
    val rolePermissionPermission = DbEnum("permission", Permission.values().toList())
    val rolePermission = Table("role_permission", rolePermissionRole, rolePermissionPermission)
    val userName = Field("name", STRING, unique = true)
    val userEmail = Field("email", STRING, unique = true)
    val userRole = DbEnum("role", Role.values().toList())
    val userSalt = Field("salt", STRING)
    val userHash = Field("hash", STRING)
    val user = Table("user", userName, userEmail, userRole, userSalt, userHash)
    val electionOwner = ForeignKey("owner", user)
    val electionName = Field("name", STRING, unique = true)
    val electionSecretBallot = Field("secret_ballot", BOOLEAN, default = "false")
    val electionNoVotingBefore = Field("no_voting_before", DATE, allowNull = true)
    val electionNoVotingAfter = Field("no_voting_after", DATE, allowNull = true)
    val electionAllowVote = Field("allow_vote", BOOLEAN, default = "false")
    val electionAllowEdit = Field("allow_edit", BOOLEAN, default = "true")
    val election = Table(
        "election",
        electionOwner,
        electionName,
        electionSecretBallot,
        electionNoVotingBefore,
        electionNoVotingAfter,
        electionAllowVote,
        electionAllowEdit
    )
    val candidateElection = ForeignKey("election", election)
    val candidateName = Field("name", STRING)
    val candidate = Table(
        "candidate",
        columns = listOf(candidateElection, candidateName),
        unique = listOf(candidateElection, candidateName)
    )
    val voterElection = ForeignKey("election", election)
    val voterUser = ForeignKey("user", user)
    val voter = Table(
        "voter",
        columns = listOf(voterElection, voterUser),
        unique = listOf(voterElection, voterUser)
    )
    val ballotUser = ForeignKey("user", user)
    val ballotElection = ForeignKey("election", election)
    val ballotConfirmation = Field("confirmation", STRING)
    val ballotWhenCast = Field("when_cast", DATE)
    val ballot = Table(
        "ballot",
        columns = listOf(ballotUser, ballotElection, ballotConfirmation, ballotWhenCast),
        unique = listOf(ballotUser, ballotElection)
    )
    val rankingBallot = ForeignKey("ballot", ballot)
    val rankingCandidate = ForeignKey("candidate", candidate)
    val rankingRank = Field("rank", INT, allowNull = true)
    val ranking = Table(
        "ranking",
        columns = listOf(rankingBallot, rankingCandidate, rankingRank),
        unique = listOf(rankingBallot, rankingCandidate)
    )
    val tallyElection = ForeignKey("election", election)
    val tallyReport = Field("report", TEXT)
    val tally = Table(
        "tally",
        columns = listOf(tallyElection, tallyReport),
        unique = listOf(tallyElection)
    )
    override val tables = listOf(
        intVariable,
        rolePermission,
        user,
        election,
        candidate,
        voter,
        ballot,
        ranking,
        tally
    )
    override val initializeQueryName: String? = "state-db-initialize"
}
