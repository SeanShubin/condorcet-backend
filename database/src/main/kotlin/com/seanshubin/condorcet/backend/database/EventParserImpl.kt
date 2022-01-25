package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.json.JsonMappers

class EventParserImpl : EventParser {
    override fun parse(name: String, content: String): Event {
        return when (name) {
            "AddUser" -> JsonMappers.parse<Event.AddUser>(content)
            "SetRole" -> JsonMappers.parse<Event.SetRole>(content)
            "RemoveUser" -> JsonMappers.parse<Event.RemoveUser>(content)
            "AddElection" -> JsonMappers.parse<Event.AddElection>(content)
            "UpdateElection" -> JsonMappers.parse<Event.UpdateElection>(content)
            "DeleteElection" -> JsonMappers.parse<Event.DeleteElection>(content)
            "AddCandidates" -> JsonMappers.parse<Event.AddCandidates>(content)
            "RemoveCandidates" -> JsonMappers.parse<Event.RemoveCandidates>(content)
            "AddVoters" -> JsonMappers.parse<Event.AddVoters>(content)
            "RemoveVoters" -> JsonMappers.parse<Event.RemoveVoters>(content)
            "CastBallot" -> JsonMappers.parse<Event.CastBallot>(content)
            "SetRankings" -> JsonMappers.parse<Event.SetRankings>(content)
            "UpdateWhenCast" -> JsonMappers.parse<Event.UpdateWhenCast>(content)
            else -> throw RuntimeException("Unsupported database operation '$name'")
        }
    }
}
