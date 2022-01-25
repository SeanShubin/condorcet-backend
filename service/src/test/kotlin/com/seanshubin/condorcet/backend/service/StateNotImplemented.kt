package com.seanshubin.condorcet.backend.service

import com.seanshubin.condorcet.backend.database.*
import com.seanshubin.condorcet.backend.domain.*
import com.seanshubin.condorcet.backend.genericdb.GenericTable
import com.seanshubin.condorcet.backend.genericdb.Schema
import java.sql.ResultSet
import java.time.Instant

interface StateNotImplemented : StateQueries, StateCommands {
    override fun setLastSynced(lastSynced: Int) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun initializeLastSynced(lastSynced: Int) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun createUser(authority: String, name: String, email: String, salt: String, hash: String, role: Role) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setRole(authority: String, name: String, role: Role) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun removeUser(authority: String, name: String) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun listUserNames(): List<String> {
        throw UnsupportedOperationException("not implemented")
    }

    override fun addElection(authority: String, owner: String, name: String) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun listElections(): List<ElectionSummary> {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateElection(authority: String, name: String, updates: ElectionUpdates) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun deleteElection(authority: String, name: String) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun findUserByName(name: String): UserRow {
        throw UnsupportedOperationException("not implemented")
    }

    override fun searchUserByName(name: String): UserRow? {
        throw UnsupportedOperationException("not implemented")
    }

    override fun searchUserByEmail(email: String): UserRow? {
        throw UnsupportedOperationException("not implemented")
    }

    override fun tableCount(): Int {
        throw UnsupportedOperationException("not implemented")
    }

    override fun userCount(): Int {
        throw UnsupportedOperationException("not implemented")
    }

    override fun electionCount(): Int {
        throw UnsupportedOperationException("not implemented")
    }

    override fun candidateCount(electionName: String): Int {
        throw UnsupportedOperationException("not implemented")
    }

    override fun voterCount(electionName: String): Int {
        throw UnsupportedOperationException("not implemented")
    }

    override fun listUsers(): List<UserRow> {
        throw UnsupportedOperationException("not implemented")
    }

    override fun roleHasPermission(role: Role, permission: Permission): Boolean {
        throw UnsupportedOperationException("not implemented")
    }

    override fun lastSynced(): Int? {
        throw UnsupportedOperationException("not implemented")
    }

    override fun searchElectionByName(name: String): ElectionSummary? {
        throw UnsupportedOperationException("not implemented")
    }

    override fun <T> queryExactlyOneRow(
        createFunction: (ResultSet) -> T,
        queryPath: String,
        vararg parameters: Any?
    ): T {
        throw UnsupportedOperationException("not implemented")
    }

    override fun <T> queryZeroOrOneRow(
        createFunction: (ResultSet) -> T,
        queryPath: String,
        vararg parameters: Any?
    ): T? {
        throw UnsupportedOperationException("not implemented")
    }

    override fun queryExists(queryPath: String, vararg parameters: Any?): Boolean {
        throw UnsupportedOperationException("not implemented")
    }

    override fun <T> query(createFunction: (ResultSet) -> T, queryPath: String, vararg parameters: Any?): List<T> {
        throw UnsupportedOperationException("not implemented")
    }

    override fun <ParentType, ChildType, KeyType, ResultType> queryJoin(
        parentFunction: (ResultSet) -> ParentType,
        childFunction: (ResultSet) -> ChildType,
        keyFunction: (ResultSet) -> KeyType,
        mergeFunction: (ParentType, List<ChildType>) -> ResultType,
        name: String,
        vararg parameters: Any?
    ): List<ResultType> {
        throw UnsupportedOperationException("not implemented")
    }

    override fun queryUntyped(name: String, code: String, vararg parameters: Any?): GenericTable {
        throw UnsupportedOperationException("not implemented")
    }

    override fun queryExactlyOneInt(queryPath: String, vararg parameters: Any?): Int {
        throw UnsupportedOperationException("not implemented")
    }

    override fun queryZeroOrOneInt(queryPath: String, vararg parameters: Any?): Int? {
        throw UnsupportedOperationException("not implemented")
    }

    override fun update(queryPath: String, vararg parameters: Any?): Int {
        throw UnsupportedOperationException("not implemented")
    }

    override fun tableNames(schema: Schema): List<String> {
        throw UnsupportedOperationException("not implemented")
    }

    override fun tableData(schema: Schema, name: String): GenericTable {
        throw UnsupportedOperationException("not implemented")
    }

    override fun debugTableData(schema: Schema, name: String): GenericTable {
        throw UnsupportedOperationException("not implemented")
    }

    override fun addCandidates(authority: String, electionName: String, candidateNames: List<String>) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun removeCandidates(authority: String, electionName: String, candidateNames: List<String>) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun addVoters(authority: String, electionName: String, voterNames: List<String>) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun removeVoters(authority: String, electionName: String, voterNames: List<String>) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun listCandidates(electionName: String): List<String> {
        throw UnsupportedOperationException("not implemented")
    }

    override fun castBallot(
        authority: String,
        voterName: String,
        electionName: String,
        rankings: List<Ranking>,
        confirmation: String,
        now: Instant
    ) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setRankings(authority: String, voterName: String, electionName: String, rankings: List<Ranking>) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateWhenCast(authority: String, confirmation: String, now: Instant) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun listRankings(voterName: String, electionName: String): List<Ranking> {
        throw UnsupportedOperationException("not implemented")
    }

    override fun searchBallot(voterName: String, electionName: String): BallotSummary? {
        throw UnsupportedOperationException("not implemented")
    }

    override fun listBallots(electionName: String): List<Ballot> {
        throw UnsupportedOperationException("not implemented")
    }

    override fun listRankings(electionName: String): List<VoterElectionRankingRow> {
        throw UnsupportedOperationException("not implemented")
    }

    override fun debugQuery(sql: String) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateUsingScript(name: String) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateUsingSqlList(name: String, sqlList: List<String>) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun purgeDatabase(name: String) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun createDatabase(name: String) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun useDatabase(name: String) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun listVoterNames(): List<String> {
        throw UnsupportedOperationException("not implemented")
    }

    override fun listVotersForElection(electionName: String): List<String> {
        throw UnsupportedOperationException("not implemented")
    }

    override fun listPermissions(role: Role): List<Permission> {
        throw UnsupportedOperationException("not implemented")
    }
}
