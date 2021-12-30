package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.domain.Permission
import com.seanshubin.condorcet.backend.domain.Ranking
import com.seanshubin.condorcet.backend.domain.Role
import com.seanshubin.condorcet.backend.genericdb.GenericDatabase

interface StateDbQueries : GenericDatabase {
    fun findUserByName(name: String): UserRow
    fun searchUserByName(name: String): UserRow?
    fun searchUserByEmail(email: String): UserRow?
    fun userCount(): Int
    fun electionCount(): Int
    fun tableCount(): Int
    fun listUsers(): List<UserRow>
    fun listElections(): List<ElectionRow>
    fun roleHasPermission(role: Role, permission: Permission): Boolean
    fun lastSynced(): Int?
    fun searchElectionByName(name: String): ElectionRow?
    fun listCandidates(electionName: String): List<String>
    fun listRankings(voterName:String, electionName:String):List<Ranking>
    fun searchBallot(voterName:String, electionName:String):BallotRow?
}
