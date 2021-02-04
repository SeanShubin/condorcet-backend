package com.seanshubin.condorcet.backend.genericdb

class SchemaInitializer(
    private val getConnection: () -> ConnectionWrapper,
    private val schema: Schema
) : Initializer {
    override fun purgeAllData() {
        getConnection().update("drop database if exists ${schema.name}")
    }

    override fun initialize() {
        if (needsInitialize()) {
            createDatabase()
            useDatabase()
            createSchema()
        } else {
            useDatabase()
        }
    }

    private fun needsInitialize(): Boolean {
        val hasSchema = "select count(*) from information_schema.schemata where schema_name = ?"
        return getConnection().queryExactlyOneInt(hasSchema, schema.name) == 0
    }

    private fun createDatabase() {
        getConnection().update("create database ${schema.name}")
    }

    private fun useDatabase() {
        getConnection().update("use ${schema.name}")
    }

    private fun createSchema() {
        val createTableStatements = schema.tables.flatMap { it.toCreateTableStatements() }
        createTableStatements.forEach {
            getConnection().update(it)
        }
    }
}

