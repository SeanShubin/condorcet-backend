package com.seanshubin.condorcet.backend.dependencies

import com.seanshubin.condorcet.backend.database.ImmutableDbSchema
import com.seanshubin.condorcet.backend.database.MutableDbSchema
import com.seanshubin.condorcet.backend.genericdb.*

class InitializerDependencies(
    integration: Integration,
    connection: ConnectionWrapper,
    val lookupImmutableSchemaName:() -> String,
    val lookupMutableSchemaName:() -> String
    ) {
    private val eventTableEvent: (GenericTable) -> Unit = integration.eventTableEvent
    private val stateTableEvent: (GenericTable) -> Unit = integration.stateTableEvent
    private val queryLoader: QueryLoader = QueryLoaderFromResource()
    private val genericDatabase: GenericDatabase = GenericDatabaseImpl(connection, queryLoader)
    private val eventSchemaCreator: SchemaCreator =
        SchemaCreatorImpl(
            genericDatabase,
            lookupImmutableSchemaName,
            ImmutableDbSchema,
            eventTableEvent
        )
    private val stateSchemaCreator: SchemaCreator =
        SchemaCreatorImpl(
            genericDatabase,
            lookupMutableSchemaName,
            MutableDbSchema,
            stateTableEvent
        )
    val schemaCreator: SchemaCreator = CompositeSchemaCreator(eventSchemaCreator, stateSchemaCreator)
}
