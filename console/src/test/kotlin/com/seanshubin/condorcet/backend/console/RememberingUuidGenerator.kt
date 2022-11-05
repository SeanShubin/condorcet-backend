package com.seanshubin.condorcet.backend.console

import com.seanshubin.condorcet.backend.crypto.UniqueIdGenerator
import com.seanshubin.condorcet.backend.string.util.ByteArrayFormat
import com.seanshubin.condorcet.backend.string.util.ByteArrayFormatServiceLocator
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

class RememberingUuidGenerator(
    private val backing: UniqueIdGenerator,
    private val path: Path
) : UniqueIdGenerator {
    var index = 0

    override fun uniqueId(): ByteArray {
        val previous: MutableList<String> = if (Files.exists(path)) Files.readAllLines(path) else mutableListOf()
        return if (index < previous.size) {
            val result = byteArrayFormat.decode(previous[index])
            index++
            result
        } else {
            val result = backing.uniqueId()
            val resultString = byteArrayFormat.encodeCompact(result)
            previous.add(resultString)
            index++
            Files.write(path, listOf(resultString), StandardOpenOption.CREATE, StandardOpenOption.APPEND)
            result
        }
    }

    companion object {
        val byteArrayFormat: ByteArrayFormat = ByteArrayFormatServiceLocator.byteArrayFormat
    }
}
