package com.seanshubin.condorcet.backend.http

data class Cookie(val name: String, val value: String) {
    companion object {
        fun parse(s: String): Cookie {
            val parts = s.split("=")
            val name = parts[0]
            val value = parts[1]
            return Cookie(name, value)
        }
    }
}
