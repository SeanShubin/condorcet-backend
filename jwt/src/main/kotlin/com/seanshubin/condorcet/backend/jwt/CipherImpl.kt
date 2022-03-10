package com.seanshubin.condorcet.backend.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm

class CipherImpl(algorithmFactory: AlgorithmFactory) : Cipher {
    private val algorithm: Algorithm = algorithmFactory.create()
    private val verifier = JWT.require(algorithm).build()

    override fun encode(map: Map<String, String>): String {
        val jwt = JWT.create()
        map.forEach { (key, value) ->
            jwt.withClaim(key, value)
        }
        val result = jwt.sign(algorithm)
        return result
    }

    override fun decode(token: String): Map<String, String?> {
        val decoded = verifier.verify(token)
        val result = decoded.claims.keys.associateWith { key ->
            decoded.claims[key]?.asString()
        }
        return result
    }
}
