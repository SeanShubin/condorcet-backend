package com.seanshubin.condorcet.backend.configuration

interface Configuration {
    fun createStringLookup(default:String, path:List<String>):() -> String
}