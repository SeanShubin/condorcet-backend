package com.seanshubin.condorcet.backend.service

interface ServiceRequestParser {
    fun parse(name: String, content: String): ServiceRequest
}
