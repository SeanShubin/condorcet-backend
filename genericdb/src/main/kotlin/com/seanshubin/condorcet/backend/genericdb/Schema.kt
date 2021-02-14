package com.seanshubin.condorcet.backend.genericdb

interface Schema {
    val tables: List<Table>
    val initializeQueryName: String?
}
