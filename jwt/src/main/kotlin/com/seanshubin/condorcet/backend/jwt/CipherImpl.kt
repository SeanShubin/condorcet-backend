package com.seanshubin.condorcet.backend.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import java.time.Clock
import java.time.Duration
import java.util.*

class CipherImpl(
    private val algorithmFactory: AlgorithmFactory,
    private val clock: Clock
) : Cipher {
    override fun encode(map: Map<String, String>, validFor: Duration): String {
        val algorithm: Algorithm = algorithmFactory.create()
        val jwt = JWT.create()
        val now = clock.instant()
        val expireInstant = now.plus(validFor)
        val expireDate = Date.from(expireInstant)
        jwt.withExpiresAt(expireDate)
        map.forEach { (key, value) ->
            jwt.withClaim(key, value)
        }
        val result = jwt.sign(algorithm)
        return result
    }

    override fun decode(token: String): Map<String, String> {
        val algorithm: Algorithm = algorithmFactory.create()
        val baseVerification = JWT.require(algorithm) as JWTVerifier.BaseVerification
        val verifier = baseVerification.build(clock)
        val decoded = verifier.verify(token)
        val result = decoded.claims.keys.mapNotNull { key ->
            val value = decoded.claims[key]?.asString()
            if (value == null) null
            else Pair(key, value)
        }.toMap()
        return result
    }
}
