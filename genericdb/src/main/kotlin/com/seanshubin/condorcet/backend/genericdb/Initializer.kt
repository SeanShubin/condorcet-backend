package com.seanshubin.condorcet.backend.genericdb

interface Initializer {
    fun purgeAllData()
    fun initialize()
}
