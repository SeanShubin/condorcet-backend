package com.seanshubin.condorcet.backend.console

import com.seanshubin.condorcet.backend.crypto.UniqueIdGenerator
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

class RememberingUuidGenerator(
    private val backing: UniqueIdGenerator,
    private val path: Path
) : UniqueIdGenerator {
    var index = 0

    override fun uniqueId(): String {
        val previous: MutableList<String> = if (Files.exists(path)) Files.readAllLines(path) else mutableListOf()
        return if (index < previous.size) {
            val result = previous[index]
            index++
            result
        } else {
            val result = backing.uniqueId()
            previous.add(result)
            index++
            Files.write(path, listOf(result), StandardOpenOption.CREATE, StandardOpenOption.APPEND)
            result
        }
    }
}
