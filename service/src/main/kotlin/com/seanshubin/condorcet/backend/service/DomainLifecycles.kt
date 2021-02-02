package com.seanshubin.condorcet.backend.service

import com.seanshubin.condorcet.backend.genericdb.ConnectionWrapper
import com.seanshubin.condorcet.backend.genericdb.Lifecycle

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
