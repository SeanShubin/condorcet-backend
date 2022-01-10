package com.seanshubin.condorcet.backend.service

object CaseInsensitiveStringListUtil {
    fun List<String>.missing(newList: List<String>): List<String> {
        val newListLowerCase = newList.map { it.lowercase() }
        return this.filter { !newListLowerCase.contains(it.lowercase()) }.distinctBy { it.lowercase() }
    }

    fun List<String>.extra(newList: List<String>): List<String> {
        val thisLowerCase = this.map { it.lowercase() }
        return newList.filter { !thisLowerCase.contains(it.lowercase()) }.distinctBy { it.lowercase() }
    }
}