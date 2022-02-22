package com.seanshubin.condorcet.backend.service

import arrow.core.Either
import arrow.core.Either.Left
import arrow.core.Either.Right
import arrow.core.flatMap
import com.seanshubin.condorcet.backend.service.RegexConstants.whitespaceBlock

object Validation {
    private val trim: (String) -> Either<String, String> = { s: String ->
        Right(s.trim())
    }
    private val minSize: (Int) -> (String) -> Either<String, String> = { x: Int ->
        { s: String ->
            if (s.length < x)
                Left("'$s' must not be less than $x characters long, was ${s.length} characters long")
            else
                Right(s)
        }
    }
    private val min1 = minSize(1)
    private val maxSize: (Int) -> (String) -> Either<String, String> = { x: Int ->
        { s: String ->
            if (s.length > x)
                Left("'$s' must not be more than $x characters long, was ${s.length} characters long")
            else
                Right(s)
        }
    }
    private val max200 = maxSize(200)

    private val requireCharQuantity: (Char, Int) -> (String) -> Either<String, String> = { c, quantity ->
        { s ->
            val actualQuantity = s.filter { it == c }.length
            if (actualQuantity == quantity) {
                Right(s)
            } else {
                Left("expected exactly $quantity of '$c' character, got $actualQuantity")
            }
        }
    }
    private val exactlyOneAtSign = requireCharQuantity('@', 1)
    private val collapseWhitespace: (String) -> Either<String, String> = { s ->
        Right(s.replace(whitespaceBlock, " "))
    }
    private val noWhitespace: (String) -> Either<String, String> = { s: String ->
        if (s.contains(whitespaceBlock)) Left("must not contain whitespace")
        else Right(s)
    }
    val userName: (String) -> Either<String, String> = { s: String ->
        trim(s).flatMap(collapseWhitespace).flatMap(min1).flatMap(max200)
    }
    val electionName: (String) -> Either<String, String> = { s: String ->
        trim(s).flatMap(collapseWhitespace).flatMap(min1).flatMap(max200)
    }
    val candidateName: (String) -> Either<String, String> = { s: String ->
        trim(s).flatMap(collapseWhitespace).flatMap(min1).flatMap(max200)
    }
    val email: (String) -> Either<String, String> = { s: String ->
        trim(s).flatMap(noWhitespace).flatMap(exactlyOneAtSign).flatMap(max200)
    }
    val password: (String) -> Either<String, String> = { s: String ->
        trim(s).flatMap(min1).flatMap(max200)
    }
    val nameOrEmail: (String) -> Either<String, String> = { s: String ->
        trim(s).flatMap(collapseWhitespace)
    }
}
