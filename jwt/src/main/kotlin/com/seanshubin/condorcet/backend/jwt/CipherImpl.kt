package com.seanshubin.condorcet.backend.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey

class CipherImpl : Cipher {
    var algorithm: Algorithm

    init {
        val kpg = KeyPairGenerator.getInstance("RSA")
        kpg.initialize(1024)
        val kp = kpg.generateKeyPair()
        val publicKey: RSAPublicKey = kp.public as RSAPublicKey
        val privateKey: RSAPrivateKey = kp.private as RSAPrivateKey
        algorithm = Algorithm.RSA256(publicKey, privateKey)
    }

    override fun decode(token: String): DecodedJWT = JWT.decode(token)

    override fun encode(map: Map<String, String>): String {
        val jwt = JWT.create()
        map.forEach { (key, value) ->
            jwt.withClaim(key, value)
        }
        return jwt.sign(algorithm)
    }
}