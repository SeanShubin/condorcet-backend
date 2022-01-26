package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.domain.*
import com.seanshubin.condorcet.backend.genericdb.GenericDatabase

interface StateQueries : GenericDatabase {
    fun findUserByName(name: String): User
    fun searchUserByName(name: String): User?
    fun searchUserByEmail(email: String): User?
    fun userCount(): Int
    fun electionCount(): Int
    fun candidateCount(electionName:String):Int
    fun voterCount(electionName:String):Int
    fun tableCount(): Int
    fun listUsers(): List<User>
    fun listElections(): List<ElectionSummary>
    fun roleHasPermission(role: Role, permission: Permission): Boolean
    fun lastSynced(): Int?
    fun searchElectionByName(name: String): ElectionSummary?
    fun listCandidates(electionName: String): List<String>
    fun listRankings(voterName: String, electionName: String): List<Ranking>
    fun listRankings(electionName: String): List<VoterElectionRankingRow>
    fun searchBallot(voterName: String, electionName: String): BallotSummary?
    fun listBallots(electionName: String): List<Ballot>
    fun listVoterNames():List<String>
    fun listVotersForElection(electionName:String):List<String>
    fun listUserNames():List<String>
    fun listPermissions(role:Role):List<Permission>
}
