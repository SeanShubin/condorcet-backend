package com.seanshubin.condorcet.backend.database

class CompositeInitializer(private vararg val initializers: Initializer) : Initializer {
    override fun reset() {
        initializers.forEach { it.reset() }
    }

    override fun initialize() {
        initializers.forEach { it.initialize() }
    }
}
