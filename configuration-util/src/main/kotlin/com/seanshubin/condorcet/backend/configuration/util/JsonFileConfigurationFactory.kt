package com.seanshubin.condorcet.backend.configuration.util

import com.fasterxml.jackson.module.kotlin.readValue
import com.seanshubin.condorcet.backend.configuration.util.Converters.IntConverter
import com.seanshubin.condorcet.backend.configuration.util.Converters.StringConverter
import com.seanshubin.condorcet.backend.contract.FilesContract
import com.seanshubin.condorcet.backend.json.JsonMappers.parser
import com.seanshubin.condorcet.backend.json.JsonMappers.pretty
import java.nio.file.Path
import java.time.Instant

class JsonFileConfigurationFactory(
    private val files: FilesContract,
    private val configFilePath: Path
) : ConfigurationFactory {
    override fun intAt(default: Any?, keys: List<String>): ConfigurationElement<Int> =
        genericElement(IntConverter, default, keys)

    override fun stringAt(default: Any?, keys: List<String>): ConfigurationElement<String> =
        genericElement(StringConverter, default, keys)

    private fun <T> genericElement(
        converter: Converter<T>,
        default: Any?,
        keys: List<String>
    ): ConfigurationElement<T> {
        val loadFunction = genericLoadFunction(converter, default, keys)
        val storeFunction = genericStoreFunction<T>(keys)
        return object : ConfigurationElement<T> {
            override val load: () -> T get() = loadFunction
            override val store: (T) -> Unit get() = storeFunction
        }
    }

    private fun <T> genericLoadFunction(converter: Converter<T>, default: Any?, keys: List<String>): () -> T = {
        val untyped = loadUntyped(default.toJsonType(), keys)
        val value = untyped.value
        val typed = converter.convert(value)
        if (typed == null) {
            val expectedType = converter.sourceType.simpleName
            val valueType = value?.javaClass?.simpleName ?: "null type"
            val pathString = keys.joinToString(".")
            val message = "At path $pathString, expected type $expectedType, got $valueType for: $value"
            throw RuntimeException(message)
        } else {
            typed
        }
    }

    private fun <T> genericStoreFunction(keys: List<String>): (T) -> Unit = { value ->
        storeUntyped(value.toJsonType(), keys)
    }

    private fun Any?.toJsonType(): Any? =
        when (this) {
            null -> null
            is String -> this
            is Int -> this
            is Long -> this
            is Path -> this.toString()
            is Instant -> this.toString()
            is List<*> -> this
            else -> {
                val typeName = this.javaClass.name
                throw RuntimeException("Don't know how to convert '$this' of type '$typeName' to a JSON type")
            }
        }

    private fun loadUntyped(default: Any?, keys: List<String>): Untyped {
        val untyped = loadConfig()
        return if (untyped.hasValueAtPath(*keys.toTypedArray())) {
            Untyped(untyped.getValueAtPath(*keys.toTypedArray()))
        } else {
            val newUntyped = untyped.setValueAtPath(default, *keys.toTypedArray())
            val jsonText = pretty.writeValueAsString(newUntyped.value)
            files.writeString(configFilePath, jsonText)
            loadUntyped(default, keys)
        }
    }

    private fun storeUntyped(value: Any?, keys: List<String>) {
        val untyped = loadConfig()
        val newUntyped = untyped.setValueAtPath(value, *keys.toTypedArray())
        val jsonText = pretty.writeValueAsString(newUntyped.value)
        files.writeString(configFilePath, jsonText)
    }

    private fun loadConfig(): Untyped {
        ensureFileExists()
        val text = files.readString(configFilePath)
        val untyped = Untyped(parser.readValue<Any?>(text))
        return untyped
    }

    private fun ensureFileExists() {
        if (!files.exists(configFilePath)) {
            files.writeString(configFilePath, "{}")
        }
    }
}
