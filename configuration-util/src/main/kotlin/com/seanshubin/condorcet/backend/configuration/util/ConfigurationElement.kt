package com.seanshubin.condorcet.backend.configuration.util

interface ConfigurationElement<T> {
    val load:() -> T
    val store:(T)->Unit
}
