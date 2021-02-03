package com.seanshubin.condorcet.backend.genericdb

class CompositeInitializer(private vararg val initializers: Initializer) : Initializer {
    override fun purgeAllData() {
        initializers.forEach { it.purgeAllData() }
    }

    override fun initialize() {
        initializers.forEach { it.initialize() }
    }
}
