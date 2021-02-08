package com.seanshubin.condorcet.backend.prototype

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.seanshubin.condorcet.backend.table.RowStyleTableFormatter
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
        val javaClock = JavaClock.systemUTC()
        val customClock = CustomClock(javaClock)
        val now = javaClock.instant()
        val fiveMinutesFromNow = now.plus(5, ChronoUnit.MINUTES)
        val fiveMinutesAgo = now.minus(5, ChronoUnit.MINUTES)
        val algorithm: Algorithm = Algorithm.HMAC256("secret")
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