package com.seanshubin.condorcet.backend.domain

import com.fasterxml.jackson.module.kotlin.readValue

class ApiParser : Parser {
    override fun parse(name: String, json: String): Command {
        return when (name) {
            "AddUser" -> parse<Command.AddUser>(json)
            "Authenticate" -> parse<Command.Authenticate>(json)
            else -> throw RuntimeException("Unsupported command '$name'")
        }
    }

    private inline fun <reified T> parse(json: String): T = JsonMappers.parser.readValue(json)
}
