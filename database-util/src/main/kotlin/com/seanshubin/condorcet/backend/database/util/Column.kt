package com.seanshubin.condorcet.backend.database.util

interface Column {
    fun toSql(): List<String>
    fun sqlName(): String
}
