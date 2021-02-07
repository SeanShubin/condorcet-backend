package com.seanshubin.condorcet.backend.service

interface ServiceEventParser {
    fun parse(name: String, content: String): ServiceRequest
}
