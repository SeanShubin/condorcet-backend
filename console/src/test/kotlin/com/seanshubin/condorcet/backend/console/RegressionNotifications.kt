package com.seanshubin.condorcet.backend.console

import com.seanshubin.condorcet.backend.console.RegressionFile.*
import com.seanshubin.condorcet.backend.http.RequestValue
import com.seanshubin.condorcet.backend.http.ResponseValue
import com.seanshubin.condorcet.backend.server.Notifications

class RegressionNotifications(
    regressionFileMap: Map<RegressionFile, RegressionInfoFile>
) : Notifications {

    val event = regressionFileMap.getValue(EVENT)
    val state = regressionFileMap.getValue(STATE)
    val http = regressionFileMap.getValue(HTTP)

    init {
        regressionFileMap.values.forEach { it.initialize() }
    }

    override fun eventDatabaseEvent(statement: String) {
        event.println(formatStatement(statement))
    }

    override fun stateDatabaseEvent(statement: String) {
        state.println(formatStatement(statement))
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
