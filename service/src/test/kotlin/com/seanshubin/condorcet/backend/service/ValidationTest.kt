package com.seanshubin.condorcet.backend.service

import arrow.core.Either
import org.junit.Test
import kotlin.test.assertEquals

class ValidationTest {
    @Test
    fun nameValidation() {
        valid("aaa", "aaa")
        notValid("", "'' must not be less than 1 characters long, was 0 characters long")
        notValid(
            "a".repeat(300),
            "'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa' must not be more than 200 characters long, was 300 characters long"
        )
        valid("\r\n\t ".repeat(200) + "bbb" + "\r\n\t ".repeat(200), "bbb")
        valid("aaa \r \n \t bbb \r \n \t ccc", "aaa bbb ccc")
    }

    fun valid(input: String, expected: String) {
        val actual = Validation.name(input)
        assertEquals(Either.Right(expected), actual)
    }

    fun notValid(input: String, expected: String) {
        val actual = Validation.name(input)
        assertEquals(Either.Left(expected), actual)
    }
}