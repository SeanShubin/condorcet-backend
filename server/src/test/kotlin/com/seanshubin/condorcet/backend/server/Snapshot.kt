package com.seanshubin.condorcet.backend.server

import com.seanshubin.condorcet.backend.genericdb.GenericTable

data class Snapshot(
    val events: List<RegressionTestEvent>,
    val eventTables: List<GenericTable>,
    val stateTables: List<GenericTable>,
    val sqlEventStatements: List<String>,
    val sqlStateStatements: List<String>
)
