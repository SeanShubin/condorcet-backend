package com.seanshubin.condorcet.backend.genericdb

import java.sql.SQLException

interface SchemaCreator {
    fun purgeAllData()
    fun initialize()
    fun listAllData()
    fun listAllDebugData()

    companion object {
        fun isDatabaseMissing(ex: SQLException): Boolean =
            ex.message?.contains("Unknown database", ignoreCase = true) == true
    }
}
