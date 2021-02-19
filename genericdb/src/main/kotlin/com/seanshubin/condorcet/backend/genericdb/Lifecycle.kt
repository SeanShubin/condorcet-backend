package com.seanshubin.condorcet.backend.genericdb

interface Lifecycle<T> {
    fun <U> withValue(f: (T) -> U): U
}
