package com.seanshubin.condorcet.backend.service

import com.seanshubin.condorcet.backend.json.JsonMappers.parse
import com.seanshubin.condorcet.backend.json.JsonUtil.normalizeJson

class ServiceEventParserImpl : ServiceEventParser {
    override fun parse(name: String, text: String): ServiceRequest {
        return try {
            val json = if (text.isBlank()) {
                "{}"
            } else {
                text.normalizeJson()
            }
            parseJson(name, json)
        } catch (ex: Exception) {
            ServiceRequest.MalformedJson(name, text)
        }
    }

    private fun parseJson(name: String, json: String): ServiceRequest {
        return when (name) {
            "AddUser" -> parse<ServiceRequest.AddUser>(json)
            "Authenticate" -> parse<ServiceRequest.Authenticate>(json)
            "SetRole" -> parse<ServiceRequest.SetRole>(json)
            "Health" -> parse<ServiceRequest.Health>(json)
            else -> ServiceRequest.Unsupported(name, json)
        }
    }
}
