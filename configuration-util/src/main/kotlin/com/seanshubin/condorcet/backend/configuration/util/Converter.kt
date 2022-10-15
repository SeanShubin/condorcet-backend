package com.seanshubin.condorcet.backend.configuration.util

interface Converter<T> {
    val sourceType: Class<*>
    fun convert(value: Any?): T?
}
