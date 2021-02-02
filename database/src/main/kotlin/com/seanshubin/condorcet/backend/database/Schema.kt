package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.database.util.DbEnum
import com.seanshubin.condorcet.backend.database.util.Table

interface Schema {
    val name: String
    val tables: List<Table>
    val enums: List<DbEnum>
}
