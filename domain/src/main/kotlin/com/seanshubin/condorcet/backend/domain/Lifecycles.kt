package com.seanshubin.condorcet.backend.domain

import com.seanshubin.condorcet.backend.genericdb.ConnectionWrapper

interface Lifecycles {
    fun openAll()
    fun closeAll()
    val eventConnection: ConnectionWrapper
    val stateConnection: ConnectionWrapper
}