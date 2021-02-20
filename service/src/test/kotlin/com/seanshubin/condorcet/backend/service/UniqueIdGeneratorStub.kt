package com.seanshubin.condorcet.backend.service

import com.seanshubin.condorcet.backend.crypto.UniqueIdGenerator

class UniqueIdGeneratorStub(val sample: Sample) : UniqueIdGenerator {
    val list = mutableListOf<String>()
    override fun uniqueId(): String {
        val uniqueId = sample.uniqueId()
        list.add(uniqueId)
        return uniqueId
    }
}