package com.seanshubin.condorcet.backend.service.http

interface ServiceCommandParser {
    fun parse(name: String, content: String): ServiceCommand
}