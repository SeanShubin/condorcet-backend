package com.seanshubin.condorcet.backend.genericdb

class SchemaInitializerDelegateToLifecycle(
    private val createInitializer: (ConnectionWrapper) -> Initializer,
    private val connectionLifecycle: Lifecycle<ConnectionWrapper>,
) : Initializer {
    override fun purgeAllData() {
        withInitializer { it.purgeAllData() }
    }

    override fun initialize() {
        withInitializer { it.initialize() }
    }

    override fun listAllData() {
        withInitializer { it.listAllData() }
    }

    private fun <T> withInitializer(f: (Initializer) -> T): T =
        connectionLifecycle.withValue { connection ->
            val initializer = createInitializer(connection)
            f(initializer)
        }
}
