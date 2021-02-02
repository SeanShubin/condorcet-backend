package com.seanshubin.condorcet.backend.genericdb

interface Column {
    fun toSql(): List<String>
    fun sqlName(): String
}
