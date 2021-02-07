package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.json.JsonMappers

class DbEventParserImpl : DbEventParser {
    override fun parse(name: String, content: String): DbEvent {
        return when (name) {
            "AddUser" -> JsonMappers.parse<DbEvent.AddUser>(content)
            "SetRole" -> JsonMappers.parse<DbEvent.SetRole>(content)
            else -> throw RuntimeException("Unsupported database operation '$name'")
        }
    }
}
