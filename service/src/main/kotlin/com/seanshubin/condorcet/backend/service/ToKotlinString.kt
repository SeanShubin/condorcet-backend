package com.seanshubin.condorcet.backend.service

import com.seanshubin.condorcet.backend.domain.*
import com.seanshubin.condorcet.backend.string.util.StringUtil.escape
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

object ToKotlinString {
    fun Unit.toKotlinString(): String = "Unit"

    fun String?.toKotlinString(): String = if (this == null) "null" else "\"${this.escape()}\""

    fun Boolean?.toKotlinString(): String = if (this == null) "null" else "$this"

    fun Int?.toKotlinString(): String = if (this == null) "null" else "$this"

    fun Pair<String, String>.toKotlinString(): String {
        return "Pair(${first.toKotlinString()}, ${second.toKotlinString()})"
    }

    fun Map<String, String>.toKotlinString(): String {
        val pairs = map { it.toPair().toKotlinString() }.joinToString(", ")
        return "mapOf(${pairs})"
    }

    fun Instant?.toKotlinString(): String = if (this == null) "null" else "Instant.parse(\"$this\")"

    fun Permission.toKotlinString(): String = this.toString()

    fun Role.toKotlinString(): String = toString()

    fun RefreshToken.toKotlinString(): String =
        "RefreshToken(${userName.toKotlinString()})"

    fun AccessToken.toKotlinString(): String =
        "AccessToken(${userName.toKotlinString()}, ${role.toKotlinString()})"

    fun Tokens.toKotlinString(): String =
        "Tokens(${refreshToken.toKotlinString()},${accessToken.toKotlinString()})"

    fun UserNameRole.toKotlinString(): String =
        "UserNameRole(${userName.toKotlinString()}, ${role.toKotlinString()}, ${
            allowedRoles.map { it.toKotlinString() }.toKotlinString()
        })"

    fun UserNameEmail.toKotlinString(): String =
        "UserNameEmail(${name.toKotlinString()}, ${email.toKotlinString()})"

    fun ElectionSummary.toKotlinString(): String =
        "ElectionSummary(" +
                "${ownerName.toKotlinString()}, " +
                "${electionName.toKotlinString()}, " +
                "${secretBallot.toKotlinString()}, " +
                "${noVotingBefore.toKotlinString()}, " +
                "${noVotingAfter.toKotlinString()}, " +
                "${allowEdit.toKotlinString()}, " +
                "${allowVote.toKotlinString()})"

    fun ElectionDetail.toKotlinString(): String =
        "ElectionDetail(" +
                "${ownerName.toKotlinString()}, " +
                "${electionName.toKotlinString()}, " +
                "${candidateCount.toKotlinString()}, " +
                "${voterCount.toKotlinString()}, " +
                "${secretBallot.toKotlinString()}, " +
                "${noVotingBefore.toKotlinString()}, " +
                "${noVotingAfter.toKotlinString()}, " +
                "${allowEdit.toKotlinString()}, " +
                "${allowVote.toKotlinString()})"

    fun ElectionUpdates.toKotlinString(): String =
        "ElectionUpdates(" +
                "${newElectionName.toKotlinString()}, " +
                "${secretBallot.toKotlinString()}, " +
                "${clearNoVotingBefore.toKotlinString()}, " +
                "${noVotingBefore.toKotlinString()}, " +
                "${clearNoVotingAfter.toKotlinString()}, " +
                "${noVotingAfter})"

    fun UserUpdates.toKotlinString(): String =
        "UserUpdates(" +
                "${userName.toKotlinString()}, " +
                "${email.toKotlinString()})"

    fun VoterEligibility.toKotlinString(): String =
        "VoterEligibility(${voterName.toKotlinString()}, ${eligible.toKotlinString()})"

    private fun Preference.toKotlinString(): String =
        path.windowed(2).zip(strengths).joinToString(" + ") { (ab, c) ->
            val (a, b) = ab
            """Preference("$a", $c, "$b")"""
        }

    fun Ranking.toKotlinString(): String =
        "Ranking(${candidateName.toKotlinString()}, ${rank.toKotlinString()})"

    private fun Ballot.toKotlinString(): String = when (this) {
        is SecretBallot -> this.toKotlinString()
        is RevealedBallot -> this.toKotlinString()
        else -> throw UnsupportedOperationException("Unknown Ballot type ${this.javaClass.name}")
    }

    private fun RevealedBallot.toKotlinString(): String =
        "RevealedBallot(" +
                "${voterName.toKotlinString()}, " +
                "${electionName.toKotlinString()}, " +
                "${confirmation.toKotlinString()}, " +
                "${whenCast.toKotlinString()}, " +
                rankings.map { it.toKotlinString() }.toKotlinString() +
                ")"

    private fun SecretBallot.toKotlinString(): String =
        "SecretBallot(" +
                "${electionName.toKotlinString()}, " +
                "${confirmation.toKotlinString()}, " +
                rankings.map { it.toKotlinString() }.toKotlinString() +
                ")"

    fun BallotSummary?.toKotlinString(): String = if (this == null) "null" else
        "BallotSummary(" +
                "${voterName.toKotlinString()}, " +
                "${electionName.toKotlinString()}, " +
                "${confirmation.toKotlinString()}, " +
                "${whenCast.toKotlinString()})"

    private fun Place.toKotlinString(): String = """Place($rank, "$candidateName")"""

    fun Tally.toKotlinString(): String =
        "Tally(" +
                "${electionName.toKotlinString()}, " +
                "${candidateNames.map { it.toKotlinString() }.toKotlinString()}, " +
                "${secretBallot.toKotlinString()}, " +
                "${ballots.map { it.toKotlinString() }.toKotlinString()}, " +
                "${listListPreferenceToKotlinString(preferences)}, " +
                "${listListPreferenceToKotlinString(strongestPathMatrix)}, " +
                "${places.map { it.toKotlinString() }.toKotlinString()}, " +
                "${whoVoted.map { it.toKotlinString() }.toKotlinString()})"

    fun TableData.toKotlinString(): String {
        val columnNamesKotlinString = columnNames.map { it.toKotlinString() }.toKotlinString()
        val rowsKotlinString = listListAnyToKotlinString(rows)
        return "TableData($columnNamesKotlinString, $rowsKotlinString)"
    }

    private fun anyToKotlinString(any: Any?): String = when (any) {
        null -> "null"
        is String -> any.toKotlinString()
        is Int -> any.toKotlinString()
        is Boolean -> any.toKotlinString()
        is LocalDateTime -> any.toInstant(ZoneOffset.UTC).toKotlinString()
        else -> throw UnsupportedOperationException("${any.javaClass.name} not implemented")
    }

    private fun listAnyToKotlinString(list: List<Any?>): String =
        list.map { anyToKotlinString(it) }.toKotlinString()

    private fun listListAnyToKotlinString(list: List<List<Any?>>): String =
        list.map { listAnyToKotlinString(it) }.toKotlinString()

    private fun listPreferenceToKotlinString(list: List<Preference>): String =
        list.map { it.toKotlinString() }.toKotlinString()

    private fun listListPreferenceToKotlinString(list: List<List<Preference>>): String =
        list.map { listPreferenceToKotlinString(it) }.toKotlinString()

    fun List<String>.toKotlinString(): String =
        joinToString(",", "listOf(", ")") { it }
}
