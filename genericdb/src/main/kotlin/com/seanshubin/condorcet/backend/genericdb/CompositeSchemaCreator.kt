package com.seanshubin.condorcet.backend.genericdb

class CompositeSchemaCreator(private vararg val schemaCreators: SchemaCreator) : SchemaCreator {
    override fun purgeAllData() {
        schemaCreators.forEach { it.purgeAllData() }
    }

    override fun initialize() {
        schemaCreators.forEach { it.initialize() }
    }

    override fun listAllData() {
        schemaCreators.forEach { it.listAllData() }
    }

    override fun listAllDebugData() {
        schemaCreators.forEach { it.listAllDebugData() }
    }
}
