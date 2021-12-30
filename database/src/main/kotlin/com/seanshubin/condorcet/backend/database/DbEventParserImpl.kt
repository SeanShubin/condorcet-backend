package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.json.JsonMappers

class DbEventParserImpl : DbEventParser {
    override fun parse(name: String, content: String): DbEvent {
        return when (name) {
            "AddUser" -> JsonMappers.parse<DbEvent.AddUser>(content)
            "SetRole" -> JsonMappers.parse<DbEvent.SetRole>(content)
            "RemoveUser" -> JsonMappers.parse<DbEvent.RemoveUser>(content)
            "AddElection" -> JsonMappers.parse<DbEvent.AddElection>(content)
            "UpdateElection" -> JsonMappers.parse<DbEvent.UpdateElection>(content)
            "DeleteElection" -> JsonMappers.parse<DbEvent.DeleteElection>(content)
            "AddCandidates" -> JsonMappers.parse<DbEvent.AddCandidates>(content)
            "RemoveCandidates" -> JsonMappers.parse<DbEvent.RemoveCandidates>(content)
            "CastBallot" -> JsonMappers.parse<DbEvent.CastBallot>(content)
            "RescindBallot" -> JsonMappers.parse<DbEvent.RescindBallot>(content)
            else -> throw RuntimeException("Unsupported database operation '$name'")
        }
    }
}
