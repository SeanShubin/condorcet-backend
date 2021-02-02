package com.seanshubin.condorcet.backend.database

interface DbEventParser {
    fun parse(name: String, content: String): DbEvent
}