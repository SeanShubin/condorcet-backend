package com.seanshubin.condorcet.backend.crypto

import java.nio.charset.StandardCharsets
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PasswordUtilTest {
    @Test
    fun validPassword() {
        // given
        val password = "foo"
        val charset = StandardCharsets.UTF_8
        val uniqueIdGenerator = SecureRandomIdGenerator
        val oneWayHash = Sha256Hash
        val passwordUtil = PasswordUtil(uniqueIdGenerator, oneWayHash, charset)
        val saltAndHash = passwordUtil.createSaltAndHash(password)

        // when
        val result = passwordUtil.passwordMatches("foo", saltAndHash)

        // then
        assertTrue(result)
    }

    @Test
    fun invalidPassword() {
        // given
        val password = "foo"
        val charset = StandardCharsets.UTF_8
        val uniqueIdGenerator = SecureRandomIdGenerator
        val oneWayHash = Sha256Hash
        val passwordUtil = PasswordUtil(uniqueIdGenerator, oneWayHash, charset)
        val saltAndHash = passwordUtil.createSaltAndHash(password)

        // when
        val result = passwordUtil.passwordMatches("bar", saltAndHash)

        // then
        assertFalse(result)
    }
}