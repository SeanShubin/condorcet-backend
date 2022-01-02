package com.seanshubin.condorcet.backend.genericdb

interface SchemaCreator {
    fun purgeAllData()
    fun initialize()
    fun listAllData()
    fun listAllDebugData()
}
