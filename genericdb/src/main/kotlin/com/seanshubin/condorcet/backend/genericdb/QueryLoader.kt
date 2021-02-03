package com.seanshubin.condorcet.backend.genericdb

interface QueryLoader {
    fun load(name: String): String
}