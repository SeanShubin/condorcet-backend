package com.seanshubin.condorcet.backend.server

import com.seanshubin.condorcet.backend.crypto.UniqueIdGenerator
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

class RememberingUuidGenerator(val backing: UniqueIdGenerator, val path: Path) : UniqueIdGenerator {
    var index = 0
    val previous: MutableList<String> = if (Files.exists(path)) Files.readAllLines(path) else mutableListOf()

    override fun uniqueId(): String {
        if (index < previous.size) {
            val result = previous[index]
            index++
            return result
        } else {
            val result = backing.uniqueId()
            previous.add(result)
            index++
            Files.write(path, listOf(result), StandardOpenOption.CREATE, StandardOpenOption.APPEND)
            return result
        }
    }
}
