package com.seanshubin.condorcet.backend.domain

interface ServiceEventParser {
    fun parse(name: String, content: String): ServiceEvent
}
