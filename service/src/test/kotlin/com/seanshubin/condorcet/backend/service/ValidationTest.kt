package com.seanshubin.condorcet.backend.service

import arrow.core.Either
import kotlin.test.Test
import kotlin.test.assertEquals

class ValidationTest {
    @Test
    fun nameValidation() {
        valid("aaa", Validation.userName, "aaa")
        notValid("", Validation.userName, "'' must not be less than 1 characters long, was 0 characters long")
        notValid(
            "a".repeat(300),
            Validation.userName,
            "'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa' must not be more than 200 characters long, was 300 characters long"
        )
        valid("\r\n\t ".repeat(200) + "bbb" + "\r\n\t ".repeat(200), Validation.userName, "bbb")
        valid("aaa \r \n \t bbb \r \n \t ccc", Validation.userName, "aaa bbb ccc")
    }

    @Test
    fun emailValidation() {
        valid("aaa@bbb", Validation.email, "aaa@bbb")
        notValid("aaa@bbb@ccc", Validation.email, "expected exactly 1 of '@' character, got 2")
        notValid("aaa-bbb", Validation.email, "expected exactly 1 of '@' character, got 0")
    }

    fun valid(input: String, rule: (String) -> Either<String, String>, expected: String) {
        val actual = rule(input)
        assertEquals(Either.Right(expected), actual)
    }

    fun notValid(input: String, rule: (String) -> Either<String, String>, expected: String) {
        val actual = rule(input)
        assertEquals(Either.Left(expected), actual)
    }
}