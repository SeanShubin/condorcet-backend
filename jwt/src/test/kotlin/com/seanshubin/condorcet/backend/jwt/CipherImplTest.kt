package com.seanshubin.condorcet.backend.jwt

import com.auth0.jwt.exceptions.SignatureVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CipherImplTest {
    @Test
    fun roundTrip() {
        val tester = createTester()
        val original = mapOf("foo" to "bar")
        val encoded = tester.cipher.encode(original, ClockStub.minutes(1))
        val decoded = tester.cipher.decode(encoded)
        assertEquals(original, decoded)
    }

    @Test
    fun notExpiredYet() {
        val tester = createTester()
        val original = mapOf("foo" to "bar")
        val encoded = tester.cipher.encode(original, ClockStub.minutes(2))
        tester.clock.passTime(ClockStub.minutes(1))
        val decoded = tester.cipher.decode(encoded)
        assertEquals(original, decoded)
    }

    @Test
    fun expired() {
        val tester = createTester()
        val original = mapOf("foo" to "bar")
        val encoded = tester.cipher.encode(original, ClockStub.minutes(2))
        tester.clock.passTime(ClockStub.minutes(3))
        assertFailsWith<TokenExpiredException> {
            tester.cipher.decode(encoded)
        }
    }

    @Test
    fun willNotDecodeUsingNewKeys() {
        val keyStoreFactory = KeyStoreFactoryImpl()
        val oldKeyStore = keyStoreFactory.generateKeyPair()
        val newKeyStore = keyStoreFactory.generateKeyPair()
        val oldTester = createTester(oldKeyStore)
        val newTester = createTester(newKeyStore)
        val original = mapOf("foo" to "bar")
        val encoded = oldTester.cipher.encode(original, ClockStub.minutes(1))
        assertFailsWith<SignatureVerificationException> {
            newTester.cipher.decode(encoded)
        }
    }

    private fun createTester(): Tester = createTester(KeyStoreFactoryImpl().generateKeyPair())

    private fun createTester(keyStore: KeyStore): Tester =
        Tester(keyStore)

    class Tester(keyStore: KeyStore) {
        val algorithmFactory = AlgorithmFactoryImpl(keyStore)
        val clock = ClockStub()
        val cipher = CipherImpl(algorithmFactory, clock)
    }
}