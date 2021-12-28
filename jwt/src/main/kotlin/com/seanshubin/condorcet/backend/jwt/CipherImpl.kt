package com.seanshubin.condorcet.backend.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT

class CipherImpl(algorithmFactory: AlgorithmFactory) : Cipher {
    private val algorithm: Algorithm = algorithmFactory.create()

    override fun decode(token: String): DecodedJWT = JWT.decode(token)

    override fun encode(map: Map<String, String>): String {
        val jwt = JWT.create()
        map.forEach { (key, value) ->
            jwt.withClaim(key, value)
        }
        return jwt.sign(algorithm)
    }

    override fun encryptedTokenToMap(token: String): Map<String, String?> {
        val decoded = decode(token)
        return decode(token).claims.keys.map { key ->
            Pair(key, decoded.claims[key]?.asString())
        }.toMap()
    }
}
