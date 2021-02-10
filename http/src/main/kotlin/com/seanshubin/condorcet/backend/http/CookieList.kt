package com.seanshubin.condorcet.backend.http

data class CookieList(val list: List<Cookie>) {
    fun cookieValue(name: String): String? =
        list.find { it.name.equals(name, ignoreCase = true) }?.value

    companion object {
        val empty = CookieList(emptyList())
    }
}
