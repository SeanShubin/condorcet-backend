package com.seanshubin.condorcet.backend.genericdb

interface DbEventParser {
    fun parse(name: String, content: String): DbEvent
}