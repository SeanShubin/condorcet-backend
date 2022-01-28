package com.seanshubin.condorcet.backend.service

import com.seanshubin.condorcet.backend.domain.*
import com.seanshubin.condorcet.backend.domain.Role.*

interface Service {
    fun synchronize()
    fun refresh(refreshToken: RefreshToken): Tokens
    fun register(userName: String, email: String, password: String): Tokens
    fun authenticate(nameOrEmail: String, password: String): Tokens
    fun permissionsForRole(role:Role):List<Permission>
    fun setRole(accessToken: AccessToken, userName: String, role: Role)
    fun removeUser(accessToken: AccessToken, userName: String)
    fun listUsers(accessToken: AccessToken): List<UserNameRole>
    fun addElection(accessToken: AccessToken, electionName: String)
    fun launchElection(accessToken:AccessToken, electionName:String, allowEdit:Boolean)
    fun finalizeElection(accessToken:AccessToken, electionName:String)
    fun updateElection(accessToken: AccessToken, electionName: String, electionUpdates: ElectionUpdates)
    fun getElection(accessToken: AccessToken, electionName: String): ElectionDetail
    fun deleteElection(accessToken: AccessToken, electionName: String)
    fun listElections(accessToken: AccessToken): List<ElectionSummary>
    fun listTables(accessToken: AccessToken): List<String>
    fun userCount(accessToken: AccessToken): Int
    fun electionCount(accessToken: AccessToken): Int
    fun tableCount(accessToken: AccessToken): Int
    fun eventCount(accessToken: AccessToken): Int
    fun tableData(accessToken: AccessToken, tableName: String): TableData
    fun debugTableData(accessToken: AccessToken, tableName: String): TableData
    fun eventData(accessToken: AccessToken): TableData
    fun setCandidates(accessToken: AccessToken, electionName: String, candidateNames: List<String>)
    fun listCandidates(accessToken: AccessToken, electionName: String): List<String>
    fun castBallot(accessToken: AccessToken, voterName: String, electionName: String, rankings: List<Ranking>)
    fun listRankings(accessToken: AccessToken, voterName: String, electionName: String): List<Ranking>
    fun tally(accessToken: AccessToken, electionName: String): Tally
    fun listEligibility(accessToken:AccessToken, electionName:String):List<VoterEligibility>
    fun setEligibleVoters(accessToken:AccessToken, electionName:String, userNames:List<String>)
    fun isEligible(accessToken:AccessToken, userName:String, electionName:String):Boolean
    fun getBallot(accessToken:AccessToken, voterName:String, electionName:String):BallotSummary?
    fun foo(){
        register("Alice", "alice@email.com", "pass")
        Tokens(RefreshToken("Alice"),AccessToken("Alice", OWNER))

        register("Bob", "bob@email.com", "pass")
        Tokens(RefreshToken("Bob"),AccessToken("Bob", OBSERVER))

        register("Carol", "carol@email.com", "pass")
        Tokens(RefreshToken("Carol"),AccessToken("Carol", OBSERVER))

        register("Dave", "dave@email.com", "pass")
        Tokens(RefreshToken("Dave"),AccessToken("Dave", OBSERVER))

        register("Eve", "eve@email.com", "pass")
        Tokens(RefreshToken("Eve"),AccessToken("Eve", OBSERVER))

        register("Frank", "frank@email.com", "pass")
        Tokens(RefreshToken("Frank"),AccessToken("Frank", OBSERVER))

        register("Grace", "grace@email.com", "pass")
        Tokens(RefreshToken("Grace"),AccessToken("Grace", OBSERVER))

        register("Heidi", "heidi@email.com", "pass")
        Tokens(RefreshToken("Heidi"),AccessToken("Heidi", OBSERVER))

        register("Ivy", "ivy@email.com", "pass")
        Tokens(RefreshToken("Ivy"),AccessToken("Ivy", OBSERVER))

        register("Judy", "judy@email.com", "pass")
        Tokens(RefreshToken("Judy"),AccessToken("Judy", OBSERVER))

        register("Mallory", "mallory@email.com", "pass")
        Tokens(RefreshToken("Mallory"),AccessToken("Mallory", OBSERVER))

        register("Trent", "trent@email.com", "pass")
        Tokens(RefreshToken("Trent"),AccessToken("Trent", OBSERVER))

        register("Walter", "walter@email.com", "pass")
        Tokens(RefreshToken("Walter"),AccessToken("Walter", OBSERVER))

        register("Peggy", "peggy@email.com", "pass")
        Tokens(RefreshToken("Peggy"),AccessToken("Peggy", OBSERVER))

        register("Victor", "victor@email.com", "pass")
        Tokens(RefreshToken("Victor"),AccessToken("Victor", OBSERVER))

        setRole(AccessToken("Alice", OWNER), "Bob", AUDITOR)
        Unit

        setRole(AccessToken("Alice", OWNER), "Carol", AUDITOR)
        Unit

        setRole(AccessToken("Alice", OWNER), "Dave", ADMIN)
        Unit

        setRole(AccessToken("Alice", OWNER), "Eve", ADMIN)
        Unit

        setRole(AccessToken("Alice", OWNER), "Frank", ADMIN)
        Unit

        setRole(AccessToken("Alice", OWNER), "Grace", USER)
        Unit

        setRole(AccessToken("Alice", OWNER), "Heidi", USER)
        Unit

        setRole(AccessToken("Alice", OWNER), "Ivy", USER)
        Unit

        setRole(AccessToken("Alice", OWNER), "Judy", USER)
        Unit

        addElection(AccessToken("Grace", USER), "Spoiler Alert")
        Unit

        setCandidates(AccessToken("Grace", USER), "Spoiler Alert", listOf("Minor Improvements","Status Quo","Radical Changes"))
        Unit

        launchElection(AccessToken("Grace", USER), "Spoiler Alert", false)
        Unit

        castBallot(AccessToken("Alice", OWNER), "Alice", "Spoiler Alert", listOf(Ranking("Status Quo", 2),Ranking("Radical Changes", null),Ranking("Minor Improvements", 1)))
        Unit

        castBallot(AccessToken("Bob", AUDITOR), "Bob", "Spoiler Alert", listOf(Ranking("Minor Improvements", 1),Ranking("Status Quo", 2),Ranking("Radical Changes", 3)))
        Unit

        castBallot(AccessToken("Carol", AUDITOR), "Carol", "Spoiler Alert", listOf(Ranking("Radical Changes", 4),Ranking("Minor Improvements", 1),Ranking("Status Quo", 2)))
        Unit

        castBallot(AccessToken("Dave", ADMIN), "Dave", "Spoiler Alert", listOf(Ranking("Minor Improvements", 2),Ranking("Radical Changes", null),Ranking("Status Quo", 1)))
        Unit

        castBallot(AccessToken("Eve", ADMIN), "Eve", "Spoiler Alert", listOf(Ranking("Status Quo", 1),Ranking("Radical Changes", 3),Ranking("Minor Improvements", 2)))
        Unit

        castBallot(AccessToken("Frank", ADMIN), "Frank", "Spoiler Alert", listOf(Ranking("Radical Changes", 3),Ranking("Minor Improvements", 2),Ranking("Status Quo", 1)))
        Unit

        castBallot(AccessToken("Grace", USER), "Grace", "Spoiler Alert", listOf(Ranking("Radical Changes", 1),Ranking("Minor Improvements", 2),Ranking("Status Quo", 3)))
        Unit

        castBallot(AccessToken("Heidi", USER), "Heidi", "Spoiler Alert", listOf(Ranking("Status Quo", null),Ranking("Radical Changes", 1),Ranking("Minor Improvements", 2)))
        Unit

        castBallot(AccessToken("Ivy", USER), "Ivy", "Spoiler Alert", listOf(Ranking("Minor Improvements", 20),Ranking("Radical Changes", 10),Ranking("Status Quo", 30)))
        Unit

        castBallot(AccessToken("Judy", USER), "Judy", "Spoiler Alert", listOf(Ranking("Minor Improvements", 2),Ranking("Status Quo", 3),Ranking("Radical Changes", 1)))
        Unit

        addElection(AccessToken("Heidi", USER), "Opening Move")
        Unit

        updateElection(AccessToken("Heidi", USER), "Opening Move", ElectionUpdates(null, false, null, null, null, null))
        Unit

        setCandidates(AccessToken("Heidi", USER), "Opening Move", listOf("Rock","Paper","Scissors"))
        Unit

        launchElection(AccessToken("Heidi", USER), "Opening Move", false)
        Unit

        castBallot(AccessToken("Alice", OWNER), "Alice", "Opening Move", listOf(Ranking("Rock", 1),Ranking("Paper", 2),Ranking("Scissors", 3)))
        Unit

        castBallot(AccessToken("Bob", AUDITOR), "Bob", "Opening Move", listOf(Ranking("Rock", 1),Ranking("Paper", 2),Ranking("Scissors", 3)))
        Unit

        castBallot(AccessToken("Carol", AUDITOR), "Carol", "Opening Move", listOf(Ranking("Rock", 1),Ranking("Paper", 2),Ranking("Scissors", 3)))
        Unit

        castBallot(AccessToken("Dave", ADMIN), "Dave", "Opening Move", listOf(Ranking("Paper", 1),Ranking("Scissors", 2),Ranking("Rock", 3)))
        Unit

        castBallot(AccessToken("Eve", ADMIN), "Eve", "Opening Move", listOf(Ranking("Paper", 1),Ranking("Scissors", 2),Ranking("Rock", 3)))
        Unit

        castBallot(AccessToken("Frank", ADMIN), "Frank", "Opening Move", listOf(Ranking("Paper", 1),Ranking("Scissors", 2),Ranking("Rock", 3)))
        Unit

        castBallot(AccessToken("Grace", USER), "Grace", "Opening Move", listOf(Ranking("Scissors", 1),Ranking("Rock", 2),Ranking("Paper", 3)))
        Unit

        castBallot(AccessToken("Heidi", USER), "Heidi", "Opening Move", listOf(Ranking("Scissors", 1),Ranking("Rock", 2),Ranking("Paper", 3)))
        Unit

        castBallot(AccessToken("Ivy", USER), "Ivy", "Opening Move", listOf(Ranking("Scissors", 1),Ranking("Rock", 2),Ranking("Paper", 3)))
        Unit

        castBallot(AccessToken("Judy", USER), "Judy", "Opening Move", listOf(Ranking("Rock", 1),Ranking("Paper", 2),Ranking("Scissors", 3)))
        Unit

        finalizeElection(AccessToken("Heidi", USER), "Opening Move")
        Unit

        addElection(AccessToken("Ivy", USER), "Credible Compromise")
        Unit

        updateElection(AccessToken("Ivy", USER), "Credible Compromise", ElectionUpdates(null, false, null, null, null, null))
        Unit

        setCandidates(AccessToken("Ivy", USER), "Credible Compromise", listOf("Relatively Unknown","Wide Appeal","Obscure A","Obscure B","Obscure C"))
        Unit

        launchElection(AccessToken("Ivy", USER), "Credible Compromise", false)
        Unit

        castBallot(AccessToken("Alice", OWNER), "Alice", "Credible Compromise", listOf(Ranking("Relatively Unknown", 1),Ranking("Wide Appeal", 2)))
        Unit

        castBallot(AccessToken("Bob", AUDITOR), "Bob", "Credible Compromise", listOf(Ranking("Relatively Unknown", 1),Ranking("Wide Appeal", 2)))
        Unit

        castBallot(AccessToken("Carol", AUDITOR), "Carol", "Credible Compromise", listOf(Ranking("Obscure A", 1),Ranking("Wide Appeal", 2)))
        Unit

        castBallot(AccessToken("Dave", ADMIN), "Dave", "Credible Compromise", listOf(Ranking("Obscure B", 1),Ranking("Wide Appeal", 2)))
        Unit

        castBallot(AccessToken("Eve", ADMIN), "Eve", "Credible Compromise", listOf(Ranking("Obscure C", 1),Ranking("Wide Appeal", 2)))
        Unit

        finalizeElection(AccessToken("Ivy", USER), "Credible Compromise")
        Unit

        addElection(AccessToken("Judy", USER), "Pets")
        Unit

        updateElection(AccessToken("Judy", USER), "Pets", ElectionUpdates(null, false, null, null, null, null))
        Unit

        setCandidates(AccessToken("Judy", USER), "Pets", listOf("Cat","Dog","Fish","Bird","Snake","Spider","Lizard","Ferret"))
        Unit

        launchElection(AccessToken("Judy", USER), "Pets", false)
        Unit

        castBallot(AccessToken("Alice", OWNER), "Alice", "Pets", listOf(Ranking("Dog", 1),Ranking("Cat", 2),Ranking("Lizard", 3),Ranking("Fish", 4),Ranking("Spider", 5),Ranking("Snake", 6),Ranking("Bird", 7),Ranking("Ferret", 8)))
        Unit

        castBallot(AccessToken("Bob", AUDITOR), "Bob", "Pets", listOf(Ranking("Dog", 1),Ranking("Snake", 2),Ranking("Spider", 3),Ranking("Lizard", 4),Ranking("Cat", 5),Ranking("Fish", 6),Ranking("Bird", 7),Ranking("Ferret", 8)))
        Unit

        castBallot(AccessToken("Carol", AUDITOR), "Carol", "Pets", listOf(Ranking("Spider", 1),Ranking("Snake", 2),Ranking("Ferret", 3),Ranking("Cat", 4),Ranking("Dog", 5),Ranking("Lizard", 6),Ranking("Bird", 7),Ranking("Fish", 8)))
        Unit

        castBallot(AccessToken("Dave", ADMIN), "Dave", "Pets", listOf(Ranking("Fish", 2),Ranking("Bird", 1),Ranking("Lizard", 3),Ranking("Dog", 4),Ranking("Spider", 8),Ranking("Ferret", 6),Ranking("Cat", 7),Ranking("Snake", 5)))
        Unit

        finalizeElection(AccessToken("Judy", USER), "Pets")
        Unit


    }
}
