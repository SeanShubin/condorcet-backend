package com.seanshubin.condorcet.backend.configuration.util

import com.fasterxml.jackson.module.kotlin.readValue
import com.seanshubin.condorcet.backend.contract.FilesContract
import com.seanshubin.condorcet.backend.json.JsonMappers
import java.nio.file.Path

class JsonFileConfigurationFactory(
    private val configurationPath: Path,
    private val files: FilesContract
) : ConfigurationFactory {
    override fun createStringLookup(default: Any, path: List<String>): () -> String {
        fun lookupString(): String {
            val theObject = loadObject(default, path)
            val theString = castToString(theObject, path)
            return theString
        }
        return ::lookupString
    }

    override fun createIntLookup(default: Any, path: List<String>): () -> Int {
        fun lookupInt(): Int {
            val theObject = loadObject(default, path)
            val theInt = castToInt(theObject, path)
            return theInt
        }
        return ::lookupInt
    }

    private fun loadObject(default: Any, path: List<String>): Any? {
        if (!files.exists(configurationPath)) {
            storeObject(default, path)
        }
        val text = files.readString(configurationPath)
        val model: Any? = JsonMappers.parser.readValue(text)
        val value = getObject(model, default, path)
        storeObject(value, path)
        return value
    }

    private fun storeObject(value: Any?, path: List<String>) {
        if (!files.exists(configurationPath)) {
            if (configurationPath.parent != null && !files.exists(configurationPath.parent)) {
                files.createDirectories(configurationPath.parent)
            }
            files.writeString(configurationPath, "{}")
        }
        val oldText = files.readString(configurationPath)
        val oldModel: Any? = JsonMappers.parser.readValue(oldText)
        val newModel = putObject(oldModel, value, path)
        val newText = JsonMappers.pretty.writeValueAsString(newModel)
        files.writeString(configurationPath, newText)
    }

    private fun putObject(untypedDestination: Any?, value: Any?, path: List<String>): Map<String, Any?> {
        val destination = castToJsonObject(untypedDestination, path)
        val head = path[0]
        val tail = path.drop(1)
        return if (path.size == 1) {
            destination + (head to value)
        } else {
            val existing = destination[head]
            if (existing == null) {
                destination + (head to putObject(emptyMap<String, Any?>(), value, tail))
            } else {
                destination + (head to putObject(existing, value, tail))
            }
        }
    }

    private fun getObject(untypedSource: Any?, default: Any?, path: List<String>): Any? {
        val source = castToJsonObject(untypedSource, path)
        val head = path[0]
        val tail = path.drop(1)
        return if (path.size == 1) {
            if (source.containsKey(head)) {
                source[head]
            } else {
                default
            }
        } else {
            val existing = source[head]
            if (existing == null) {
                default
            } else {
                getObject(existing, default, tail)
            }
        }
    }

    private fun castToString(value: Any?, path: List<String>): String =
        when (value) {
            null -> throwCastError(null, "null", "String", path)
            is String -> value
            else -> throwCastError(value, value.javaClass.simpleName, "String", path)
        }

    private fun castToInt(value: Any?, path: List<String>): Int =
        when (value) {
            null -> throwCastError(null, "null", "Int", path)
            is Int -> value
            else -> throwCastError(value, value.javaClass.simpleName, "Int", path)
        }

    @Suppress("UNCHECKED_CAST")
    private fun castToJsonObject(value: Any?, path: List<String>): Map<String, Any?> =
        when (value) {
            null -> throwCastError(null, "null", "Map<String, Object>", path)
            is Map<*, *> -> value as Map<String, Any?>
            else -> throwCastError(value, value.javaClass.simpleName, "Map<String, Object>", path)
        }

    private fun throwCastError(
        value: Any?,
        sourceTypeName: String,
        destinationTypeName: String,
        path: List<String>
    ): Nothing {
        val joinedPath = path.joinToString(".")
        val message = "Unable to cast value $value of type $sourceTypeName to $destinationTypeName at path $joinedPath"
        throw ClassCastException(message)
    }
}