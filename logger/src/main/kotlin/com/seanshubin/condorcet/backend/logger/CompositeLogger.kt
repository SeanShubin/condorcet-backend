package com.seanshubin.condorcet.backend.logger

class CompositeLogger(private vararg val loggers: Logger) : Logger {
    override fun log(lines: List<String>) {
        loggers.forEach { it.log(lines) }
    }
}
