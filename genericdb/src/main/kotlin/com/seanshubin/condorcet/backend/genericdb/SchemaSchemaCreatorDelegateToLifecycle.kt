package com.seanshubin.condorcet.backend.genericdb

class SchemaSchemaCreatorDelegateToLifecycle(
    private val createSchemaCreator: (ConnectionWrapper) -> SchemaCreator,
    private val connectionLifecycle: Lifecycle<ConnectionWrapper>,
) : SchemaCreator {
    override fun purgeAllData() {
        withInitializer { it.purgeAllData() }
    }

    override fun initialize() {
        withInitializer { it.initialize() }
    }

    override fun listAllData() {
        withInitializer { it.listAllData() }
    }

    override fun listAllDebugData() {
        withInitializer { it.listAllDebugData() }
    }

    private fun <T> withInitializer(f: (SchemaCreator) -> T): T =
        connectionLifecycle.withValue { connection ->
            val initializer = createSchemaCreator(connection)
            f(initializer)
        }
}
