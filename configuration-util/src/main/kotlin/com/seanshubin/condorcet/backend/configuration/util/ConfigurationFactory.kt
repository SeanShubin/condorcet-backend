package com.seanshubin.condorcet.backend.configuration.util

interface ConfigurationFactory {
    fun intAt(default: Any?, keys: List<String>): ConfigurationElement<Int>
    fun stringAt(default: Any?, keys: List<String>): ConfigurationElement<String>
}
