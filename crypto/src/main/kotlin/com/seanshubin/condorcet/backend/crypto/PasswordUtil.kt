package com.seanshubin.condorcet.backend.crypto

import java.nio.charset.Charset

class PasswordUtil(
    private val uniqueIdGenerator: UniqueIdGenerator,
    private val oneWayHash: OneWayHash,
    private val charset: Charset
) {
    fun createSaltAndHash(password: String): SaltAndHash =
        SaltAndHash.fromPassword(password, uniqueIdGenerator, oneWayHash, charset)

    fun passwordMatches(password: String, saltAndHash: SaltAndHash): Boolean =
        SaltAndHash.validate(password, saltAndHash, oneWayHash, charset)

    fun passwordMatches(password: String, salt: String, hash: String): Boolean =
        SaltAndHash.validate(password, SaltAndHash(salt, hash), oneWayHash, charset)
}
