package com.seanshubin.condorcet.backend.crypto

import java.util.*

class Uuid4 : UniqueIdGenerator {
    override fun uniqueId(): String {
        val uuid = UUID.randomUUID()
        return uuid.toString()
    }
}
