package com.seanshubin.condorcet.backend.genericdb

interface Schema {
    val name: String
    val tables: List<Table>
    val initializeQueryName: String?
}
