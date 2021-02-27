package com.seanshubin.condorcet.backend.prototype

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue

object JsonApp {
    val objectMapper: ObjectMapper = ObjectMapper().registerModule(KotlinModule()).registerModule(JavaTimeModule())
        .enable(SerializationFeature.INDENT_OUTPUT)
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

    @JvmStatic
    fun main(args: Array<String>) {
        val exists =
            """{
              |  "name" : "abc",
              |  "value" : 123
              |}
              |""".trimMargin()
        val explicitNull =
            """{
              |  "name" : "abc",
              |  "value" : null
              |}
              |""".trimMargin()
        val omitted =
            """{
              |  "name" : "abc"
              |}
              |""".trimMargin()
        println(parse(exists))
        println(parse(explicitNull))
        println(parse(omitted))
    }

    fun parse(json: String): MyObject {
        val map = objectMapper.readValue<Map<String, Any?>>(json)
        val name: String? = map["name"] as String?
        val clearValue = map.containsKey("value") && map["value"] == null
        val value: Int? = map["value"] as Int?
        return MyObject(name, clearValue, value)
    }
}