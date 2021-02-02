package com.seanshubin.condorcet.backend.domain

import com.seanshubin.condorcet.backend.database.util.ConnectionWrapper
import com.seanshubin.condorcet.backend.database.util.Lifecycle

class DomainLifecycles(
    private val eventConnectionLifecycle: Lifecycle<ConnectionWrapper>,
    private val stateConnectionLifecycle: Lifecycle<ConnectionWrapper>
) : Lifecycles {
    override fun openAll() {
        eventConnectionLifecycle.open()
        stateConnectionLifecycle.open()
    }

    override fun closeAll() {
        stateConnectionLifecycle.close()
        eventConnectionLifecycle.close()
    }

    override val eventConnection: ConnectionWrapper
        get() = eventConnectionLifecycle.getValue()
    override val stateConnection: ConnectionWrapper
        get() = stateConnectionLifecycle.getValue()
}
