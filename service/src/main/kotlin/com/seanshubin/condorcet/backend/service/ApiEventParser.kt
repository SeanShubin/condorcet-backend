package com.seanshubin.condorcet.backend.service

import com.seanshubin.condorcet.backend.json.JsonMappers.parse
import com.seanshubin.condorcet.backend.json.JsonUtil.normalizeJson

class ServiceEventParserImpl : ServiceEventParser {
    override fun parse(name: String, text: String): ServiceEvent {
        return try {
            val json = if (text.isBlank()) {
                "{}"
            } else {
                text.normalizeJson()
            }
            parseJson(name, json)
        } catch (ex: Exception) {
            ServiceEvent.MalformedJson(name, text)
        }
    }

    private fun parseJson(name: String, json: String): ServiceEvent {
        return when (name) {
            "AddUser" -> parse<ServiceEvent.AddUser>(json)
            "Authenticate" -> parse<ServiceEvent.Authenticate>(json)
            "Health" -> parse<ServiceEvent.Health>(json)
            else -> ServiceEvent.Unsupported(name, json)
        }
    }
}
