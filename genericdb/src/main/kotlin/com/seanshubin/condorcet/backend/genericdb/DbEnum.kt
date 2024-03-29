package com.seanshubin.condorcet.backend.genericdb

data class DbEnum<T : Enum<T>>(
    val name: String,
    val values: List<T>,
    val allowNull: Boolean = false,
    val unique: Boolean = false
) : Column {
    override fun toSql(): List<String> {
        val parts = mutableListOf<String>()
        parts.add(handleKeyword(name))
        parts.add(values.joinToString("','", "ENUM('", "')"))
        if (!allowNull) {
            parts.add("not null")
        }
        if (unique) {
            parts.add("unique")
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
