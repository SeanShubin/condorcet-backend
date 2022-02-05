package com.seanshubin.condorcet.backend.dependencies

interface ConfigurationLookupFunctions {
    val lookupDatabaseHost:()->String
    val lookupDatabaseUser:()->String
    val lookupDatabasePassword:()->String
    val lookupDatabasePort:()->Int
    val lookupServerPort:()->Int
}
