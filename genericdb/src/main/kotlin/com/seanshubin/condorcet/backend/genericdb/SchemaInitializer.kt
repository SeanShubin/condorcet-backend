package com.seanshubin.condorcet.backend.genericdb

class SchemaInitializer(
    private val connection: ConnectionWrapper,
    private val schemaName: String,
    private val schema: Schema,
    private val queryLoader: QueryLoader,
    private val listTableEvent: (GenericTable) -> Unit
) : Initializer {
    override fun purgeAllData() {
        connection.update("purgeAllData()", "drop database if exists $schemaName")
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
            val tableData = connection.queryGenericTable("select * from ${it.name}")
            listTableEvent(tableData)
        }
    }

    private fun needsInitialize(): Boolean {
        val hasSchema = "select count(*) from information_schema.schemata where schema_name = ?"
        return connection.queryExactlyOneInt("needsInitialize()", hasSchema, schemaName) == 0
    }

    private fun createDatabase() {
        connection.update("createDatabase()", "create database $schemaName")
    }

    private fun useDatabase() {
        connection.update("useDatabase()", "use $schemaName")
    }

    private fun createSchema() {
        val createTableStatements = schema.tables.flatMap { it.toCreateTableStatements() }
        createTableStatements.forEach {
            connection.update("createSchema()", it)
        }
    }

    private fun staticData() {
        val initializeQueryName = schema.initializeQueryName
        if (initializeQueryName != null) {
            val initializeQuery = queryLoader.load(initializeQueryName)
            val statements = initializeQuery.split(';').map(String::trim).filter(String::isNotBlank)
            statements.forEach {
                connection.update("staticData()", it)
            }
        }
    }
}
