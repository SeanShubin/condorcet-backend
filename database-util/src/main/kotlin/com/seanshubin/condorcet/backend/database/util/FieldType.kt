package com.seanshubin.condorcet.backend.database.util

enum class FieldType(val sql: String) {
    STRING("varchar(255)"),
    DATE("datetime(6)"),
    BOOLEAN("boolean"),
    INT("int"),
    TEXT("text")
}
