package com.seanshubin.condorcet.backend.genericdb

interface Secrets {
    fun databaseHost():String
    fun databasePassword():String
}
