package com.seanshubin.condorcet.backend.genericdb

class SchemaInitializer(
    private val getConnection: () -> ConnectionWrapper,
    private val schemaName: String,
    private val schema: Schema,
    private val queryLoader: QueryLoader,
    private val afterInitialize: () -> Unit
) : Initializer {
    override fun purgeAllData() {
        getConnection().update("drop database if exists $schemaName")
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
        afterInitialize()
    }

    private fun needsInitialize(): Boolean {
        val hasSchema = "select count(*) from information_schema.schemata where schema_name = ?"
        return getConnection().queryExactlyOneInt(hasSchema, schemaName) == 0
    }

    private fun createDatabase() {
        getConnection().update("create database $schemaName")
    }

    private fun useDatabase() {
        getConnection().update("use $schemaName")
    }

    private fun createSchema() {
        val createTableStatements = schema.tables.flatMap { it.toCreateTableStatements() }
        createTableStatements.forEach {
            getConnection().update(it)
        }
    }

    private fun staticData() {
        val initializeQueryName = schema.initializeQueryName
        if (initializeQueryName != null) {
            val initializeQuery = queryLoader.load(initializeQueryName)
            val statements = initializeQuery.split(';').map(String::trim).filter(String::isNotBlank)
            statements.forEach {
                getConnection().update(it)
            }
        }
    }
}
