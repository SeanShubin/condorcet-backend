package com.seanshubin.condorcet.backend.domain

import com.fasterxml.jackson.module.kotlin.readValue

class ApiParser : Parser {
    override fun parse(name: String, text: String): Command {
        val json = if (text.isBlank()) "{}" else text
        return when (name) {
            "AddUser" -> parse<Command.AddUser>(json)
            "Authenticate" -> parse<Command.Authenticate>(json)
            "Health" -> parse<Command.Health>(json)
            else -> Command.Unsupported(name, text)
        }
    }

    private inline fun <reified T> parse(json: String): T = JsonMappers.parser.readValue(json)
}
