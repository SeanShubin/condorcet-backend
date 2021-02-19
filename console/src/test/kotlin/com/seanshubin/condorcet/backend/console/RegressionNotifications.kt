package com.seanshubin.condorcet.backend.console

import com.seanshubin.condorcet.backend.console.RegressionFile.*
import com.seanshubin.condorcet.backend.genericdb.GenericTable
import com.seanshubin.condorcet.backend.http.RequestValue
import com.seanshubin.condorcet.backend.http.ResponseValue
import com.seanshubin.condorcet.backend.server.Notifications

class RegressionNotifications(
    regressionFileMap: Map<RegressionFile, RegressionInfoFile>
) : Notifications {

    val root = regressionFileMap.getValue(ROOT)
    val event = regressionFileMap.getValue(EVENT)
    val state = regressionFileMap.getValue(STATE)
    val eventTable = regressionFileMap.getValue(EVENT_TABLE)
    val stateTable = regressionFileMap.getValue(STATE_TABLE)
    val http = regressionFileMap.getValue(HTTP)

    init {
        regressionFileMap.values.forEach { it.initialize() }
    }

    override fun rootDatabaseEvent(statement: String) {
        root.println(formatStatement(statement))
    }

    override fun eventDatabaseEvent(statement: String) {
        event.println(formatStatement(statement))
    }

    override fun stateDatabaseEvent(statement: String) {
        state.println(formatStatement(statement))
    }

    override fun eventTableEvent(table: GenericTable) {
        table.toLines().forEach(eventTable::println)
    }

    override fun stateTableEvent(table: GenericTable) {
        table.toLines().forEach(stateTable::println)
    }

    override fun requestEvent(request: RequestValue) {
        request.toLines().forEach {
            http.println(it)
        }
    }

    override fun responseEvent(response: ResponseValue) {
        response.toLines().forEach {
            http.println(it)
        }
        http.println("")
    }

    fun formatStatement(statement: String): String = statement.trim().replace(whitespace, " ") + ";"


    companion object {
        val whitespace = Regex("""\s+""")
    }
}
