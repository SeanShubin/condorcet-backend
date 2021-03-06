package com.seanshubin.condorcet.backend.io

import com.seanshubin.condorcet.backend.io.ioutil.consumeString
import java.nio.charset.StandardCharsets

object ClassLoaderUtil {
    fun loadResourceAsString(name: String): String {
        val charset = StandardCharsets.UTF_8
        val classLoader = this.javaClass.classLoader
        val inputStream = classLoader.getResourceAsStream(name)
        if (inputStream == null) {
            throw RuntimeException("Resource named '$name' not found")
        } else {
            return inputStream.consumeString(charset)
        }
    }

    fun loadResourceRelativeFunction(relativeTo: String): (String) -> String = {
        loadResourceAsString("$relativeTo/$it")
    }
}
