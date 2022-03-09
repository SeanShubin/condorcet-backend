package com.seanshubin.condorcet.backend.configuration.util

interface ConfigurationFactory {
    fun createStringLookup(default:Any, path:List<String>):() -> String
    fun createIntLookup(default:Any, path:List<String>):() -> Int
}