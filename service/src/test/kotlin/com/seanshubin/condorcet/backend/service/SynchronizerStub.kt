package com.seanshubin.condorcet.backend.service

import com.seanshubin.condorcet.backend.database.Synchronizer

class SynchronizerStub : Synchronizer {
    override fun synchronize() {
        throw UnsupportedOperationException("not implemented")
    }
}