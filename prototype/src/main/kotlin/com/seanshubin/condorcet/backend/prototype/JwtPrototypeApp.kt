package com.seanshubin.condorcet.backend.prototype

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.seanshubin.condorcet.backend.table.RowStyleTableFormatter
import java.nio.file.Files
import java.nio.file.Paths
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.KeySpec
import java.security.spec.PKCS8EncodedKeySpec
import java.time.temporal.ChronoUnit
import java.util.*
import com.auth0.jwt.interfaces.Clock as Auth0Clock
import java.time.Clock as JavaClock


object JwtPrototypeApp {
    class CustomClock(val javaClock: JavaClock) : Auth0Clock {
        override fun getToday(): Date {
            return Date.from(javaClock.instant())
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val kpg = KeyPairGenerator.getInstance("RSA")
        kpg.initialize(1024)
        val kp = kpg.generateKeyPair()
        val publicKey: RSAPublicKey = kp.public as RSAPublicKey
        val privateKey: RSAPrivateKey = kp.private as RSAPrivateKey
        val javaClock = JavaClock.systemUTC()
        val customClock = CustomClock(javaClock)
        val now = javaClock.instant()
        val fiveMinutesFromNow = now.plus(5, ChronoUnit.MINUTES)
        val fiveMinutesAgo = now.minus(5, ChronoUnit.MINUTES)
        val algorithm: Algorithm = Algorithm.RSA256(publicKey, privateKey)
        val verificationBuilder: JWTVerifier.BaseVerification = JWT.require(algorithm) as JWTVerifier.BaseVerification
        val verifier: JWTVerifier = verificationBuilder.build(customClock)
        val goodToken: String = JWT.create()
            .withIssuer("auth0")
            .withClaim("role", "ADMIN")
            .withExpiresAt(Date.from(fiveMinutesFromNow))
            .sign(algorithm)
        println(goodToken)
        display(goodToken)
        val validatedToken = verifier.verify(goodToken)
        display(validatedToken)
    }

    fun loadPublicKey(): RSAPublicKey {
        val keyFactory = KeyFactory.getInstance("RSA")
        val path = Paths.get("secrets/condorcet.cer")
        val bytes = Files.readAllBytes(path)
        val keySpec: KeySpec = PKCS8EncodedKeySpec(bytes)
        return keyFactory.generatePublic(keySpec) as RSAPublicKey
    }

    fun loadPrivateKey(): RSAPrivateKey {
        val keyFactory = KeyFactory.getInstance("RSA")
        val path = Paths.get("secrets/condorcet.key")
        val bytes = Files.readAllBytes(path)
        val keySpec: KeySpec = PKCS8EncodedKeySpec(bytes)
        return keyFactory.generatePrivate(keySpec) as RSAPrivateKey
    }

    fun display(token: String) {
        jwtTokenToLines(token).forEach(::println)
    }

    fun display(decoded: DecodedJWT) {
        decodedJwtToLines(decoded).forEach(::println)
    }

    fun jwtTokenToLines(token: String): List<String> {
        val decoded = JWT.decode(token)
        return decodedJwtToLines(decoded)
    }

    fun decodedJwtToLines(decoded: DecodedJWT): List<String> {
        val properties = listOf(
//            listOf("token", decoded.token),
//            listOf("header", decoded.header),
//            listOf("payload", decoded.payload),
            listOf("signature", decoded.signature),
            listOf("algorithm", decoded.algorithm),
            listOf("type", decoded.type),
            listOf("contentType", decoded.contentType),
            listOf("keyId", decoded.keyId),
            listOf("issuer", decoded.issuer),
            listOf("subject", decoded.subject),
            listOf("audience", decoded.audience),
            listOf("expiresAt", decoded.expiresAt),
            listOf("notBefore", decoded.notBefore),
            listOf("issuedAt", decoded.issuedAt),
            listOf("id", decoded.id)
        )
        val claims = decoded.claims.keys.map { key ->
            val value = decoded.claims[key]?.asString()
            listOf("claim[$key]", value)
        }
        val table = properties + claims
        val lines = RowStyleTableFormatter.boxDrawing.format(table)
        return lines
    }
}