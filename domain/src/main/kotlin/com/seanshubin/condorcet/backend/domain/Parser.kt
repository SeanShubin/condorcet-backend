package com.seanshubin.condorcet.backend.domain

interface Parser {
    fun parse(name: String, content: String): Command
}
