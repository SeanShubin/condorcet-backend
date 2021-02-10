package com.seanshubin.condorcet.backend.jwt

import com.auth0.jwt.interfaces.DecodedJWT

interface Cipher {
    fun decode(token: String): DecodedJWT
    fun encode(map: Map<String, String>): String
}
