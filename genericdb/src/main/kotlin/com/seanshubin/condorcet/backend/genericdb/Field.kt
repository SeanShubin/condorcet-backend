package com.seanshubin.condorcet.backend.genericdb

data class Field(
    val name: String,
    val type: FieldType,
    val allowNull: Boolean = false,
    val unique: Boolean = false,
    val default: String? = null
) : Column {
    override fun toSql(): List<String> {
        val parts = mutableListOf<String>()
        parts.add(handleKeyword(name))
        parts.add(type.sql)
        if (!allowNull) {
            parts.add("not null")
        }
        if (unique) {
            parts.add("unique")
        }
        if (default != null) {
            parts.add("default $default")
        }
        return listOf(parts.joinToString(" ") + ",")
    }

    private fun handleKeyword(s: String): String =
        if (MysqlConstants.reservedWords.contains(s.lowercase())) {
            """`$s`"""
        } else {
            s
        }

    override fun sqlName(): String = handleKeyword(name)
}
