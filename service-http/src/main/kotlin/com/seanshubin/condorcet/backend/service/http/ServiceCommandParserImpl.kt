package com.seanshubin.condorcet.backend.service.http

import com.seanshubin.condorcet.backend.json.JsonMappers
import java.time.Instant

class ServiceCommandParserImpl : ServiceCommandParser {
    override fun parse(name: String, content: String?): ServiceCommand {
        return try {
            parseJson(name, content ?: "{}")
        } catch (ex: Throwable) {
            // catch Throwable instead of Exception
            // because jackson's JsonDeserializer.getAbsentValue
            // can throw a java.lang.NoSuchMethodError
            return ServiceCommand.Malformed(name, content ?: "{}");
        }
    }

    private fun parseJson(name: String, json: String): ServiceCommand {
        return when (name) {
            "Health" -> ServiceCommand.Health
            "Refresh" -> ServiceCommand.Refresh
            "Register" -> JsonMappers.parse<ServiceCommand.Register>(json)
            "Logout" -> ServiceCommand.Logout
            "Authenticate" -> JsonMappers.parse<ServiceCommand.Authenticate>(json)
            "AuthenticateWithToken" -> JsonMappers.parse<ServiceCommand.AuthenticateWithToken>(json)
            "SetRole" -> JsonMappers.parse<ServiceCommand.SetRole>(json)
            "RemoveUser" -> JsonMappers.parse<ServiceCommand.RemoveUser>(json)
            "ListUsers" -> ServiceCommand.ListUsers
            "AddElection" -> JsonMappers.parse<ServiceCommand.AddElection>(json)
            "LaunchElection" -> JsonMappers.parse<ServiceCommand.LaunchElection>(json)
            "FinalizeElection" -> JsonMappers.parse<ServiceCommand.FinalizeElection>(json)
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
            "ListRankings" -> JsonMappers.parse<ServiceCommand.ListRankings>(json)
            "Tally" -> JsonMappers.parse<ServiceCommand.Tally>(json)
            "SetEligibleVoters" -> JsonMappers.parse<ServiceCommand.SetEligibleVoters>(json)
            "ListEligibility" -> JsonMappers.parse<ServiceCommand.ListEligibility>(json)
            "IsEligible" -> JsonMappers.parse<ServiceCommand.IsEligible>(json)
            "GetBallot" -> JsonMappers.parse<ServiceCommand.GetBallot>(json)
            "ChangePassword" -> JsonMappers.parse<ServiceCommand.ChangePassword>(json)
            "SendLoginLinkByEmail" -> JsonMappers.parse<ServiceCommand.SendLoginLinkByEmail>(json)
            "UpdateUser" -> JsonMappers.parse<ServiceCommand.UpdateUser>(json)
            "GetUser" -> JsonMappers.parse<ServiceCommand.GetUser>(json)
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
            electionName = map["electionName"] as String,
            newElectionName = map["newElectionName"] as String?,
            secretBallot = map["secretBallot"] as Boolean?,
            clearNoVotingBefore = map.containsKey("noVotingBefore") && map["noVotingBefore"] == null,
            noVotingBefore = map["noVotingBefore"].toInstant(),
            clearNoVotingAfter = map.containsKey("noVotingAfter") && map["noVotingAfter"] == null,
            noVotingAfter = map["noVotingAfter"].toInstant()
        )
    }
}
