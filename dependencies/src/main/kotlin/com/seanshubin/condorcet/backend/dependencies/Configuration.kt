package com.seanshubin.condorcet.backend.dependencies

import com.seanshubin.condorcet.backend.genericdb.DatabaseConfiguration

interface Configuration {
    val rootDatabase: DatabaseConfiguration
    val immutableDatabase: DatabaseConfiguration
    val mutableDatabase:DatabaseConfiguration
    val serverPort:()->Int
}
