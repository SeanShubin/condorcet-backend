package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.json.JsonMappers

class EventCommandParserImpl : EventCommandParser {
    override fun parse(name: String, content: String): EventCommand {
        return when (name) {
            "AddUser" -> JsonMappers.parse<EventCommand.AddUser>(content)
            "SetRole" -> JsonMappers.parse<EventCommand.SetRole>(content)
            "RemoveUser" -> JsonMappers.parse<EventCommand.RemoveUser>(content)
            "AddElection" -> JsonMappers.parse<EventCommand.AddElection>(content)
            "UpdateElection" -> JsonMappers.parse<EventCommand.UpdateElection>(content)
            "DeleteElection" -> JsonMappers.parse<EventCommand.DeleteElection>(content)
            "AddCandidates" -> JsonMappers.parse<EventCommand.AddCandidates>(content)
            "RemoveCandidates" -> JsonMappers.parse<EventCommand.RemoveCandidates>(content)
            "AddVoters" -> JsonMappers.parse<EventCommand.AddVoters>(content)
            "RemoveVoters" -> JsonMappers.parse<EventCommand.RemoveVoters>(content)
            "CastBallot" -> JsonMappers.parse<EventCommand.CastBallot>(content)
            "SetRankings" -> JsonMappers.parse<EventCommand.SetRankings>(content)
            "UpdateWhenCast" -> JsonMappers.parse<EventCommand.UpdateWhenCast>(content)
            else -> throw RuntimeException("Unsupported database operation '$name'")
        }
    }
}
