package com.seanshubin.condorcet.backend.genericdb

interface DbRow<T> {
    val primaryKey: T
    val cells: List<Any?>
}
