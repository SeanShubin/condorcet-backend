package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.database.util.ConnectionWrapper

class SchemaInitializer(
    private val getConnection: () -> ConnectionWrapper,
    private val schemaName: String
) : Initializer {
    override fun initialize() {
        if (needsInitialize()) {
            createDatabase()
            useDatabase()
            createSchema()
            createStaticData()
        } else {
            useDatabase()
        }
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
        val createTableStatements = Schema.tables.flatMap { it.toCreateTableStatements() }
        createTableStatements.forEach {
            getConnection().update(it)
        }
    }

    private fun createStaticData() {
        DbStatus.values().forEach {
            getConnection().update("insert into status (name) values (?)", it.name.toLowerCase())
        }
    }
}

