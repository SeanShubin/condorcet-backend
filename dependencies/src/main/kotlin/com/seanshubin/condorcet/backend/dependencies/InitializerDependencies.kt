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
    private val genericDatabase: GenericDatabase = GenericDatabaseImpl(connection, queryLoader)
    private val eventSchemaCreator: SchemaCreator =
        SchemaCreatorImpl(
            genericDatabase,
            eventSchemaName,
            EventSchema,
            eventTableEvent
        )
    private val stateSchemaCreator: SchemaCreator =
        SchemaCreatorImpl(
            genericDatabase,
            stateSchemaName,
            StateSchema,
            stateTableEvent
        )
    val schemaCreator: SchemaCreator = CompositeSchemaCreator(eventSchemaCreator, stateSchemaCreator)
}
