package com.seanshubin.condorcet.backend.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec

class CipherImpl(algorithmFactory:AlgorithmFactory) : Cipher {
    private val algorithm: Algorithm = algorithmFactory.create()

    override fun decode(token: String): DecodedJWT = JWT.decode(token)

    override fun encode(map: Map<String, String>): String {
        val jwt = JWT.create()
        map.forEach { (key, value) ->
            jwt.withClaim(key, value)
        }
        return jwt.sign(algorithm)
    }
}