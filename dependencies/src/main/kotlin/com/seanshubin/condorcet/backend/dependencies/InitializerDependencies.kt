package com.seanshubin.condorcet.backend.dependencies

import com.seanshubin.condorcet.backend.database.EventSchema
import com.seanshubin.condorcet.backend.database.StateSchema
import com.seanshubin.condorcet.backend.genericdb.*

class InitializerDependencies(
    integration: Integration,
    connection: ConnectionWrapper,

    ) {
    private val eventSchemaName: String = integration.eventSchemaName
    private val stateSchemaName: String = integration.stateSchemaName

    private val eventTableEvent: (GenericTable) -> Unit = integration.eventTableEvent
    private val stateTableEvent: (GenericTable) -> Unit = integration.stateTableEvent
    private val queryLoader: QueryLoader = QueryLoaderFromResource()
    private val eventInitializer: Initializer =
        SchemaInitializer(
            connection,
            eventSchemaName,
            EventSchema,
            queryLoader,
            eventTableEvent
        )
    private val stateInitializer: Initializer =
        SchemaInitializer(
            connection,
            stateSchemaName,
            StateSchema,
            queryLoader,
            stateTableEvent
        )
    val initializer: Initializer = CompositeInitializer(eventInitializer, stateInitializer)
}