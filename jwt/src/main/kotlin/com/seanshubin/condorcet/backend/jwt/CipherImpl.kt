package com.seanshubin.condorcet.backend.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm

class CipherImpl(private val algorithmFactory: AlgorithmFactory) : Cipher {
    override fun encode(map: Map<String, String>): String {
        val algorithm: Algorithm = algorithmFactory.create()
        val jwt = JWT.create()
        map.forEach { (key, value) ->
            jwt.withClaim(key, value)
        }
        val result = jwt.sign(algorithm)
        return result
    }

    override fun decode(token: String): Map<String, String?> {
        val algorithm: Algorithm = algorithmFactory.create()
        val verifier = JWT.require(algorithm).build()
        val decoded = verifier.verify(token)
        val result = decoded.claims.keys.associateWith { key ->
            decoded.claims[key]?.asString()
        }
        return result
    }
}
