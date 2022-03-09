package com.seanshubin.condorcet.backend.server

interface ServerConfiguration {
    val lookupPort:()->Int
    fun reify(){
        lookupPort()
    }
}
