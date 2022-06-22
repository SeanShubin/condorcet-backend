package com.seanshubin.condorcet.backend.jwt

import com.auth0.jwt.algorithms.Algorithm
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec

class AlgorithmFactoryImpl(private val keyStore: KeyStore) : AlgorithmFactory {
    override fun create(): Algorithm {
        val publicKeyBytes = keyStore.publicKey()
        val privateKeyBytes = keyStore.privateKey()
        val keyFactory = KeyFactory.getInstance("RSA")
        val publicKey: RSAPublicKey = keyFactory.generatePublic(X509EncodedKeySpec(publicKeyBytes)) as RSAPublicKey
        val privateKey: RSAPrivateKey =
            keyFactory.generatePrivate(PKCS8EncodedKeySpec(privateKeyBytes)) as RSAPrivateKey
        return Algorithm.RSA256(publicKey, privateKey)
    }
}
