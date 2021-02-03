package com.seanshubin.condorcet.backend.service

import com.seanshubin.condorcet.backend.genericdb.ConnectionWrapper

interface Lifecycles {
    fun openAll()
    fun closeAll()
    val eventConnection: ConnectionWrapper
    val stateConnection: ConnectionWrapper
    fun <T> doInLifecycle(f: () -> T): T {
        openAll()
        try {
            return f()
        } finally {
            closeAll()
        }
    }

}
