package com.seanshubin.condorcet.backend.jwt

interface Cipher {
    fun decode(token: String): Map<String, String?>
    fun encode(map: Map<String, String>): String
}
