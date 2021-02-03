package com.seanshubin.condorcet.backend.server

class SqlMonitorInMemory : SqlMonitor {
    private val mutableSqlStatements = mutableListOf<String>()
    override fun monitor(sql: String) {
        mutableSqlStatements.add(sql)
    }

    override fun getSqlStatements(): List<String> {
        return mutableSqlStatements.toList()
    }
}
