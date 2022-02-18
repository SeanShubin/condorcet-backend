package com.seanshubin.condorcet.backend.genericdb

interface DatabaseConfiguration {
    val lookupSchemaName:()->String
    val lookupHost:()->String
    val lookupUser:()->String
    val lookupPassword:()->String
    val lookupPort:()->Int
}
