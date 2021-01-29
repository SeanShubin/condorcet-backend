package com.seanshubin.condorcet.backend.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class ParsersTest {
    @Test
    fun parseCommandNameFromTarget() {
        // given
        val target = "/foo/bar"
        val expected = "foo"

        // when
        val actual = Parsers.parseCommandNameFromTarget(target)

        // then
        assertEquals(expected, actual)
    }
}
