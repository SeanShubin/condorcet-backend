package com.seanshubin.condorcet.backend.service.http

import com.seanshubin.condorcet.backend.json.JsonMappers
import com.seanshubin.condorcet.backend.json.JsonUtil.normalizeJson

class ServiceCommandParserImpl : ServiceCommandParser {
    override fun parse(name: String, content: String): ServiceCommand {
        return try {
            val json = if (content.isBlank()) {
                "{}"
            } else {
                content.normalizeJson()
            }
            parseJson(name, json)
        } catch (ex: Exception) {
            return ServiceCommand.Malformed(name, content);
        }
    }

    private fun parseJson(name: String, json: String): ServiceCommand {
        return when (name) {
            "Refresh" -> JsonMappers.parse<ServiceCommand.Refresh>(json)
            "Register" -> JsonMappers.parse<ServiceCommand.Register>(json)
            "Authenticate" -> JsonMappers.parse<ServiceCommand.Authenticate>(json)
            "SetRole" -> JsonMappers.parse<ServiceCommand.SetRole>(json)
            "RemoveUser" -> JsonMappers.parse<ServiceCommand.RemoveUser>(json)
            "ListUsers" -> JsonMappers.parse<ServiceCommand.ListUsers>(json)
            else -> ServiceCommand.Unsupported(name, json)
        }
    }
}
