package com.seanshubin.condorcet.backend.database.util

interface Lifecycle<T> {
    fun open()
    fun getValue(): T
    fun close()
    fun <U> withValue(f: (T) -> U): U {
        open()
        try {
            return f(getValue())
        } finally {
            close()
        }
    }
}
