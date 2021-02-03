package com.seanshubin.condorcet.backend.genericdb

import com.seanshubin.condorcet.backend.io.ioutil.consumeString
import java.nio.charset.StandardCharsets

class QueryLoaderFromResource : QueryLoader {
    override fun load(name: String): String {
        val resourceName = "sql/$name.sql"
        val charset = StandardCharsets.UTF_8
        val classLoader = this.javaClass.classLoader
        val inputStream = classLoader.getResourceAsStream(resourceName)
        if (inputStream == null) {
            throw RuntimeException("Resource named '$resourceName' not found")
        } else {
            return inputStream.consumeString(charset)
        }
    }
}
