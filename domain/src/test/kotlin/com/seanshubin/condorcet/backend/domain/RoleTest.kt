package com.seanshubin.condorcet.backend.domain

import kotlin.test.Test

class RoleTest {
    @Test
    fun roleOrdering() {
        assert(Role.OWNER > Role.AUDITOR)
        assert(Role.AUDITOR > Role.ADMIN)
        assert(Role.ADMIN > Role.USER)
        assert(Role.USER > Role.OBSERVER)
        assert(Role.OBSERVER > Role.NO_ACCESS)
    }
}