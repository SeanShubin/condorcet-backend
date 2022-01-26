package com.seanshubin.condorcet.backend.database

interface EventCommandParser {
    fun parse(name: String, content: String): EventCommand
}