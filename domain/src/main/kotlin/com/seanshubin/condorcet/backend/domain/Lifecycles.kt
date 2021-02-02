package com.seanshubin.condorcet.backend.domain

import com.seanshubin.condorcet.backend.database.util.ConnectionWrapper

interface Lifecycles {
    fun openAll()
    fun closeAll()
    val eventConnection: ConnectionWrapper
    val stateConnection: ConnectionWrapper
}