package com.seanshubin.condorcet.backend.configuration.util

object Converters {
    object StringConverter : Converter<String> {
        override val sourceType: Class<*> get() = String::class.java

        override fun convert(value: Any?): String? =
            if (value is String) value else null
    }

    object IntConverter : Converter<Int> {
        override val sourceType: Class<*> get() = Int::class.java

        override fun convert(value: Any?): Int? =
            if (value is Int) value else null
    }
}
