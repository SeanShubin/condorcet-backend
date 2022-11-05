package com.seanshubin.condorcet.backend.service

import com.seanshubin.condorcet.backend.string.util.ByteArrayFormatServiceLocator

class Sample {
    var index = 1
    fun string(): String = (index++).toString()
    fun name(): String = "name-${string()}"
    fun email(): String = "email-${string()}@email.com"
    fun password(): String = "password-${string()}"
    fun uniqueId(): ByteArray = "unique-id-${string()}".toByteArray()
    fun hash(inputBytes: ByteArray): ByteArray {
        val inputString = byteArrayFormat.encodeCompact(inputBytes)
        val outputString = "hash-for($inputString)"
        return outputString.toByteArray()
    }

    companion object {
        val byteArrayFormat = ByteArrayFormatServiceLocator.byteArrayFormat
    }
}
