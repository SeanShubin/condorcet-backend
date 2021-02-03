package com.seanshubin.condorcet.backend.server

interface RegressionTestRunner {
    fun createMissingSnapshotsForExpected()
    fun createSnapshotsForActual()
}
