package com.seanshubin.condorcet.backend.dependencies

import com.seanshubin.condorcet.backend.configuration.Configuration

class JsonConfigurationLookupFunctions(
    configuration:Configuration,
    secretsConfiguration: Configuration
):ConfigurationLookupFunctions {
    override val lookupDatabaseHost: () -> String = configuration.createStringLookup("database-host", listOf("database", "host"))
    override val lookupDatabaseUser: () -> String = configuration.createStringLookup("root", listOf("database", "user"))
    override val lookupDatabasePassword: () -> String = secretsConfiguration.createStringLookup("database-password", listOf("database", "password"))
    override val lookupDatabasePort: () -> Int  = configuration.createIntLookup(3306, listOf("database", "port"))
    override val lookupServerPort: () -> Int  = configuration.createIntLookup(8080, listOf("server", "port"))
}
