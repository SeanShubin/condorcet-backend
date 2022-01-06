package com.seanshubin.condorcet.backend.service

import arrow.core.Either
import arrow.core.Either.Left
import arrow.core.Either.Right
import arrow.core.flatMap
import com.seanshubin.condorcet.backend.service.RegexConstants.whitespaceBlock

object Validation {
    val trim: (String) -> Either<String, String> = { s: String ->
        Right(s.trim())
    }
    val minSize: (Int) -> (String) -> Either<String, String> = { x: Int ->
        { s: String ->
            if (s.length < x)
                Left("'$s' must not be less than $x characters long, was ${s.length} characters long")
            else
                Right(s)
        }
    }
    val min1 = minSize(1)
    val maxSize: (Int) -> (String) -> Either<String, String> = { x: Int ->
        { s: String ->
            if (s.length > x)
                Left("'$s' must not be more than $x characters long, was ${s.length} characters long")
            else
                Right(s)
        }
    }
    val max200 = maxSize(200)
    val collapseWhitespace: (String) -> Either<String, String> = { s: String ->
        Right(s.replace(whitespaceBlock, " "))
    }
    val name: (String) -> Either<String, String> = { s: String ->
        trim(s).flatMap { collapseWhitespace(it) }.flatMap { min1(it) }.flatMap { max200(it) }
    }
}
