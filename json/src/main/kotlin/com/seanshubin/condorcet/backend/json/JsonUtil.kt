package com.seanshubin.condorcet.backend.json

import com.fasterxml.jackson.module.kotlin.readValue
import com.seanshubin.condorcet.backend.json.JsonMappers.parser
import com.seanshubin.condorcet.backend.json.JsonMappers.pretty

object JsonUtil {
    fun String.normalizeJson(): String {
        val asObject = parser.readValue<Any>(this)
        val asNormalized = pretty.writeValueAsString(asObject)
        return asNormalized
    }
}