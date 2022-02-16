package com.seanshubin.condorcet.backend.dependencies

interface ConfigurationLookupFunctions {
    val lookupImmutableSchemaName:()->String
    val lookupMutableSchemaName:()->String
    val lookupDatabaseHost:()->String
    val lookupDatabaseUser:()->String
    val lookupDatabasePassword:()->String
    val lookupDatabasePort:()->Int
    val lookupServerPort:()->Int
}
