package com.seanshubin.condorcet.backend.crypto

interface UniqueIdGenerator {
    fun uniqueId(): ByteArray
}
