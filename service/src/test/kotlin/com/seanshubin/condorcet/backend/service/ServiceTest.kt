package com.seanshubin.condorcet.backend.service

import com.seanshubin.condorcet.backend.crypto.OneWayHash
import com.seanshubin.condorcet.backend.crypto.PasswordUtil
import com.seanshubin.condorcet.backend.domain.Role
import com.seanshubin.condorcet.backend.string.util.ByteArrayFormat
import com.seanshubin.condorcet.backend.string.util.ByteArrayFormatServiceLocator
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.time.Duration
import java.time.temporal.ChronoUnit
import kotlin.test.Test
import kotlin.test.assertEquals

class ServiceTest {
    @Test
    fun typicalRegisterUser() {
        // given
        val sample = Sample()
        val tester = Tester(sample)
        val service = tester.service
        val name = sample.name()
        val email = sample.email()
        val password = sample.password()

        // when
        service.register(
            name,
            email,
            password
        )

        // then
        val rows = tester.stateDbFake.users
        assertEquals(1, tester.stateDbFake.users.size)
        val row = rows[0]
        assertEquals(name, row.name)
        assertEquals(email, row.email)
        assertEquals(tester.uniqueIdGenerator.list[0], row.salt)
        assertEquals(Role.OWNER, row.role)
    }

    @Test
    fun collapseSpacesInUserName() {
        // given
        val sample = Sample()
        val tester = Tester(sample)
        val service = tester.service

        // when
        service.register(
            "name   with \t\r\n  whitespace",
            sample.email(),
            sample.password()
        )

        // then
        val rows = tester.stateDbFake.users
        assertEquals(1, tester.stateDbFake.users.size)
        val row = rows[0]
        assertEquals("name with whitespace", row.name)
    }

    @Test
    fun trimName() {
        // given
        val sample = Sample()
        val tester = Tester(sample)
        val service = tester.service

        // when
        service.register(
            "    name with surrounding whitespace \r\n\t   ",
            sample.email(),
            sample.password()
        )

        // then
        val rows = tester.stateDbFake.users
        assertEquals(1, tester.stateDbFake.users.size)
        val row = rows[0]
        assertEquals("name with surrounding whitespace", row.name)
    }

    class Tester(sample: Sample) {
        val uniqueIdGenerator = UniqueIdGeneratorStub(sample)
        private val clock = ClockStub()
        private val oneWayHash: OneWayHash = OneWayHashStub(sample)
        private val charset: Charset = StandardCharsets.UTF_8
        private val passwordUtil: PasswordUtil = PasswordUtil(uniqueIdGenerator, oneWayHash, charset)
        private val eventDbFake = ImmutableDbFake()
        val stateDbFake = MutableDbFake()
        private val synchronizer = SynchronizerStub()
        private val random = RandomStub()
        private val mailService = MailServiceUnsupportedOperation()
        private val emailAccessTokenExpire = Duration.of(10, ChronoUnit.MINUTES)
        private val createUpdatePasswordLink = { accessToken: AccessToken, baseUri: String -> "update-password-link" }
        private val byteArrayFormat: ByteArrayFormat = ByteArrayFormatServiceLocator.byteArrayFormat
        val service: Service = BaseService(
            passwordUtil,
            eventDbFake,
            stateDbFake,
            stateDbFake,
            synchronizer,
            random,
            clock,
            uniqueIdGenerator,
            mailService,
            emailAccessTokenExpire,
            createUpdatePasswordLink,
            byteArrayFormat
        )
    }
}
