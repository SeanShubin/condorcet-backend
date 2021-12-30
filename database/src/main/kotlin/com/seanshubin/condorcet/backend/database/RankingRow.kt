package com.seanshubin.condorcet.backend.database

data class RankingRow(val voter: String, val election: String, val candidate: String, val rank: Int)
