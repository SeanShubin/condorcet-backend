package com.seanshubin.condorcet.backend.genericdb

class SchemaCreatorImpl(
    private val database: GenericDatabase,
    private val schemaName: String,
    private val schema: Schema,
    private val listTableEvent: (GenericTable) -> Unit
) : SchemaCreator {
    override fun purgeAllData() {
        database.purgeDatabase(schemaName)
    }

    override fun initialize() {
        if (needsInitialize()) {
            createDatabase()
            useDatabase()
            createSchema()
            staticData()
        } else {
            useDatabase()
        }
    }

    override fun listAllData() {
        useDatabase()
        schema.tables.forEach {
            val tableData = database.tableData(schema, it.name)
            listTableEvent(tableData)
        }
    }

    override fun listAllDebugData() {
        useDatabase()
        schema.tables.forEach {
            val tableData = database.debugTableData(schema, it.name)
            listTableEvent(tableData)
        }
    }

    private fun needsInitialize(): Boolean {
        return database.queryExactlyOneInt("table-count", schemaName) == 0
    }

    private fun createDatabase() {
        database.createDatabase(schemaName)
    }

    private fun useDatabase() {
        database.useDatabase(schemaName)
    }

    private fun createSchema() {
        val createTableStatements = schema.tables.flatMap { it.toCreateTableStatements() }
        database.updateUsingSqlList("createSchema()", createTableStatements)
    }

    private fun staticData() {
        val initializeQueryName = schema.initializeQueryName
        if (initializeQueryName != null) {
            database.updateUsingScript(initializeQueryName)
        }
    }
}
