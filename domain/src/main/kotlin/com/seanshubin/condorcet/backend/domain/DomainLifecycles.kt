package com.seanshubin.condorcet.backend.domain

import com.seanshubin.condorcet.backend.database.util.ConnectionWrapper
import com.seanshubin.condorcet.backend.database.util.Lifecycle

class DomainLifecycles(private val connectionLifecycle: Lifecycle<ConnectionWrapper>) : Lifecycles {
    override fun openAll() {
        connectionLifecycle.open()
    }

    override fun closeAll() {
        connectionLifecycle.close()
    }

    override val connection: ConnectionWrapper
        get() = connectionLifecycle.getValue()
}
