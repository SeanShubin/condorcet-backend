package com.seanshubin.condorcet.backend.server

interface SqlMonitor {
    fun monitor(sql: String)
    fun getSqlStatements(): List<String>
}