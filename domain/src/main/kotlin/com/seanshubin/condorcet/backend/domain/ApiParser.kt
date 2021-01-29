package com.seanshubin.condorcet.backend.domain

import com.fasterxml.jackson.module.kotlin.readValue
import com.seanshubin.condorcet.backend.json.JsonMappers
import com.seanshubin.condorcet.backend.json.JsonUtil.normalizeJson

class ApiParser : Parser {
    override fun parse(name: String, text: String): Command {
        return try {
            val json = if (text.isBlank()) {
                "{}"
            } else {
                text.normalizeJson()
            }
            parseJson(name, json)
        } catch (ex: Exception) {
            Command.MalformedJson(name, text)
        }
    }

    private fun parseJson(name: String, json: String): Command {
        return when (name) {
            "AddUser" -> parse<Command.AddUser>(json)
            "Authenticate" -> parse<Command.Authenticate>(json)
            "Health" -> parse<Command.Health>(json)
            else -> Command.Unsupported(name, json)
        }
    }

    private inline fun <reified T> parse(json: String): T = JsonMappers.parser.readValue(json)
}
