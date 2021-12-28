package com.seanshubin.condorcet.backend.service.http

import com.seanshubin.condorcet.backend.json.JsonMappers
import java.time.Instant

class ServiceCommandParserImpl : ServiceCommandParser {
    override fun parse(name: String, content: String?): ServiceCommand {
        return try {
            parseJson(name, content ?: "")
        } catch (ex: Exception) {
            return ServiceCommand.Malformed(name, content ?: "");
        }
    }

    private fun parseJson(name: String, json: String): ServiceCommand {
        return when (name) {
            "Refresh" -> ServiceCommand.Refresh
            "Register" -> JsonMappers.parse<ServiceCommand.Register>(json)
            "Logout" -> ServiceCommand.Logout
            "Authenticate" -> JsonMappers.parse<ServiceCommand.Authenticate>(json)
            "SetRole" -> JsonMappers.parse<ServiceCommand.SetRole>(json)
            "RemoveUser" -> JsonMappers.parse<ServiceCommand.RemoveUser>(json)
            "ListUsers" -> ServiceCommand.ListUsers
            "AddElection" -> JsonMappers.parse<ServiceCommand.AddElection>(json)
            "UpdateElection" -> parseUpdateElection(json)
            "DeleteElection" -> JsonMappers.parse<ServiceCommand.DeleteElection>(json)
            "GetElection" -> JsonMappers.parse<ServiceCommand.GetElection>(json)
            "ListElections" -> ServiceCommand.ListElections
            "ListTables" -> ServiceCommand.ListTables
            "UserCount" -> ServiceCommand.UserCount
            "ElectionCount" -> ServiceCommand.ElectionCount
            "TableCount" -> ServiceCommand.TableCount
            "EventCount" -> ServiceCommand.EventCount
            "TableData" -> JsonMappers.parse<ServiceCommand.TableData>(json)
            "DebugTableData" -> JsonMappers.parse<ServiceCommand.DebugTableData>(json)
            "EventData" -> ServiceCommand.EventData
            "SetCandidates" -> JsonMappers.parse<ServiceCommand.SetCandidates>(json)
            "ListCandidates" -> JsonMappers.parse<ServiceCommand.ListCandidates>(json)
            "CastBallot" -> JsonMappers.parse<ServiceCommand.CastBallot>(json)
            else -> ServiceCommand.Unsupported(name, json)
        }
    }

    private fun Any?.toInstant(): Instant? {
        this as String?
        return if (this == null) {
            null
        } else {
            Instant.parse(this)
        }
    }

    private fun parseUpdateElection(json: String): ServiceCommand.UpdateElection {
        val map = JsonMappers.parse<Map<String, Any?>>(json)
        return ServiceCommand.UpdateElection(
            name = map["name"] as String,
            newName = map["newName"] as String?,
            secretBallot = map["secretBallot"] as Boolean?,
            clearScheduledStart = map.containsKey("scheduledStart") && map["scheduledStart"] == null,
            scheduledStart = map["scheduledStart"].toInstant(),
            clearScheduledEnd = map.containsKey("scheduledEnd") && map["scheduledEnd"] == null,
            scheduledEnd = map["scheduledEnd"].toInstant(),
            restrictWhoCanVote = map["restrictWhoCanVote"] as Boolean?,
            ownerCanDeleteBallots = map["ownerCanDeleteBallots"] as Boolean?,
            auditorCanDeleteBallots = map["auditorCanDeleteBallots"] as Boolean?,
            isTemplate = map["isTemplate"] as Boolean?,
            noChangesAfterVote = map["noChangesAfterVote"] as Boolean?,
            isOpen = map["isOpen"] as Boolean?
        )
    }
}
