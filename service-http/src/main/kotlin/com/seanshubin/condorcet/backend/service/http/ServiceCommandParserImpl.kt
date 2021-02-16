package com.seanshubin.condorcet.backend.service.http

import com.seanshubin.condorcet.backend.json.JsonMappers

class ServiceCommandParserImpl : ServiceCommandParser {
    override fun parse(name: String, content: String): ServiceCommand {
        return try {
            parseJson(name, content)
        } catch (ex: Exception) {
            return ServiceCommand.Malformed(name, content);
        }
    }

    private fun parseJson(name: String, json: String): ServiceCommand {
        return when (name) {
            "Refresh" -> ServiceCommand.Refresh
            "Register" -> JsonMappers.parse<ServiceCommand.Register>(json)
            "Authenticate" -> JsonMappers.parse<ServiceCommand.Authenticate>(json)
            "SetRole" -> JsonMappers.parse<ServiceCommand.SetRole>(json)
            "RemoveUser" -> JsonMappers.parse<ServiceCommand.RemoveUser>(json)
            "ListUsers" -> ServiceCommand.ListUsers
            else -> ServiceCommand.Unsupported(name, json)
        }
    }
}
