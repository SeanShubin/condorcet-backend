package com.seanshubin.condorcet.backend.dependencies

import com.seanshubin.condorcet.backend.database.ImmutableDbSchema
import com.seanshubin.condorcet.backend.database.MutableDbSchema
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
            ImmutableDbSchema,
            eventTableEvent
        )
    private val stateSchemaCreator: SchemaCreator =
        SchemaCreatorImpl(
            genericDatabase,
            stateSchemaName,
            MutableDbSchema,
            stateTableEvent
        )
    val schemaCreator: SchemaCreator = CompositeSchemaCreator(eventSchemaCreator, stateSchemaCreator)
}
