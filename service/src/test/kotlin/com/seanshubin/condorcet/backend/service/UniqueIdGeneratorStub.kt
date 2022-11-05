package com.seanshubin.condorcet.backend.service

import com.seanshubin.condorcet.backend.crypto.UniqueIdGenerator
import com.seanshubin.condorcet.backend.string.util.ByteArrayFormat
import com.seanshubin.condorcet.backend.string.util.ByteArrayFormatServiceLocator

class UniqueIdGeneratorStub(val sample: Sample) : UniqueIdGenerator {
    val list = mutableListOf<String>()
    override fun uniqueId(): ByteArray {
        val uniqueId = sample.uniqueId()
        val uniqueIdString = byteArrayFormat.encodeCompact(uniqueId)
        list.add(uniqueIdString)
        return uniqueId
    }

    companion object {
        val byteArrayFormat: ByteArrayFormat = ByteArrayFormatServiceLocator.byteArrayFormat
    }
}