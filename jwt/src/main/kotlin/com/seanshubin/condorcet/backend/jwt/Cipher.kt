package com.seanshubin.condorcet.backend.jwt

import java.time.Duration

interface Cipher {
    fun decode(token: String): Map<String, String?>
    fun encode(map: Map<String, String>, validFor: Duration): String
}
