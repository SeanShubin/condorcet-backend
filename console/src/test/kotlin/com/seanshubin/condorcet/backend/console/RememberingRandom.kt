package com.seanshubin.condorcet.backend.console

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import kotlin.random.Random

class RememberingRandom(
    private val backing: Random,
    private val path: Path
) : Random() {
    var index = 0
    override fun nextBits(bitCount: Int): Int {
        throw UnsupportedOperationException("not implemented")
    }

    override fun nextInt(from: Int, until: Int): Int {
        val previous: MutableList<String> = if (Files.exists(path)) Files.readAllLines(path) else mutableListOf()
        return if (index < previous.size) {
            val line = previous[index]
            val result = LineRegex.matchEntire(line)!!.groupValues[3].toInt()
            index++
            result
        } else {
            val result = backing.nextInt(from, until)
            val resultLine = "from $from until $until resulted in $result"
            previous.add(resultLine)
            index++
            Files.write(path, listOf(resultLine), StandardOpenOption.CREATE, StandardOpenOption.APPEND)
            result
        }
    }

    companion object {
        val LineRegex = Regex("""from (\d+) until (\d+) resulted in (\d+)""")
    }
}
