package com.seanshubin.condorcet.backend.database

interface EventParser {
    fun parse(name: String, content: String): Event
}