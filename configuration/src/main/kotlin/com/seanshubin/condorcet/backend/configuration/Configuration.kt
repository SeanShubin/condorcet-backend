package com.seanshubin.condorcet.backend.configuration

interface Configuration {
    fun createStringLookup(default:Any, path:List<String>):() -> String
    fun createIntLookup(default:Any, path:List<String>):() -> Int
}
