package com.seanshubin.condorcet.backend.database.util

interface Lifecycle<T> {
    fun open()
    fun getValue(): T
    fun close()
}