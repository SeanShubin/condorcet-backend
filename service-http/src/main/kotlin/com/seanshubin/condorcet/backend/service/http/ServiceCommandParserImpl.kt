package com.seanshubin.condorcet.backend.service.http

import com.seanshubin.condorcet.backend.json.JsonMappers

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
            "UpdateElection" -> JsonMappers.parse<ServiceCommand.UpdateElection>(json)
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
            else -> ServiceCommand.Unsupported(name, json)
        }
    }
}
