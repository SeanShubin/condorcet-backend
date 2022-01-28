package com.seanshubin.condorcet.backend.domain

data class VoterElectionCandidateRank(val voter: String, val election: String, val candidate: String, val rank: Int)
