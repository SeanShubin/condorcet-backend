package com.seanshubin.condorcet.backend.service

import com.seanshubin.condorcet.backend.json.JsonMappers.parse
import com.seanshubin.condorcet.backend.json.JsonUtil.normalizeJson

class ServiceRequestParserImpl : ServiceRequestParser {
    override fun parse(name: String, content: String): ServiceRequest {
        return try {
            val json = if (content.isBlank()) {
                "{}"
            } else {
                content.normalizeJson()
            }
            parseJson(name, json)
        } catch (ex: Exception) {
            throw ServiceException.MalformedJson("malformed json\n$content")
        }
    }

    private fun parseJson(name: String, json: String): ServiceRequest {
        return when (name) {
            "Refresh" -> parse<ServiceRequest.Refresh>(json)
            "Register" -> parse<ServiceRequest.Register>(json)
            "Authenticate" -> parse<ServiceRequest.Authenticate>(json)
            "SetRole" -> parse<ServiceRequest.SetRole>(json)
            "RemoveUser" -> parse<ServiceRequest.RemoveUser>(json)
            "ListUsers" -> parse<ServiceRequest.ListUsers>(json)
            else -> throw ServiceException.Unsupported("unsupported command $name\n$json")
        }
    }
}
