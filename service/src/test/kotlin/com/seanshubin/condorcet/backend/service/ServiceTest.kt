package com.seanshubin.condorcet.backend.service

import com.seanshubin.condorcet.backend.crypto.OneWayHash
import com.seanshubin.condorcet.backend.crypto.PasswordUtil
import com.seanshubin.condorcet.backend.domain.Role
import org.junit.Test
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
        val rows = tester.stateDbFake.userRows
        assertEquals(1, tester.stateDbFake.userRows.size)
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
        val rows = tester.stateDbFake.userRows
        assertEquals(1, tester.stateDbFake.userRows.size)
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
        val rows = tester.stateDbFake.userRows
        assertEquals(1, tester.stateDbFake.userRows.size)
        val row = rows[0]
        assertEquals("name with surrounding whitespace", row.name)
    }

    class Tester(sample: Sample) {
        val uniqueIdGenerator = UniqueIdGeneratorStub(sample)
        private val clock = ClockStub()
        private val oneWayHash: OneWayHash = OneWayHashStub(sample)
        private val passwordUtil: PasswordUtil = PasswordUtil(uniqueIdGenerator, oneWayHash)
        private val eventDbFake = EventDbFake()
        val stateDbFake = StateDbFake()
        private val synchronizer = SynchronizerStub()
        private val random = RandomStub()
        val service: Service = BaseService(
            passwordUtil,
            eventDbFake,
            stateDbFake,
            stateDbFake,
            synchronizer,
            random,
            clock,
            uniqueIdGenerator
        )
    }
}
