package com.seanshubin.condorcet.backend.database.util

interface DbRow<T> {
    val primaryKey: T
    val cells: List<Any?>
}
