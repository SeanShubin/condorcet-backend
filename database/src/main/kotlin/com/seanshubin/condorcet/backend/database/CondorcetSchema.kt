package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.database.util.DbEnum
import com.seanshubin.condorcet.backend.database.util.Field
import com.seanshubin.condorcet.backend.database.util.FieldType.*
import com.seanshubin.condorcet.backend.database.util.ForeignKey
import com.seanshubin.condorcet.backend.database.util.Table

object CondorcetSchema : Schema {
    val userName = Field("name", STRING, unique = true)
    val userEmail = Field("email", STRING, unique = true)
    val userSalt = Field("salt", STRING)
    val userHash = Field("hash", STRING)
    val user = Table("user", userName, userEmail, userSalt, userHash)
    val status = DbEnum.fromEnum<Status>()
    val electionOwner = ForeignKey("owner", user)
    val electionName = Field("name", STRING, unique = true)
    val electionEnd = Field("end", DATE, allowNull = true)
    val electionSecret = Field("secret", BOOLEAN)
    val electionStatus = ForeignKey("status", status.table)
    val election = Table(
        "election",
        electionOwner,
        electionName,
        electionEnd,
        electionSecret,
        electionStatus
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
    override val name: String = "condorcet_state"
    override val tables = listOf(
        user,
        status.table,
        election,
        candidate,
        voter,
        ballot,
        ranking,
        tally
    )
    override val enums: List<DbEnum> = listOf(status)
}
