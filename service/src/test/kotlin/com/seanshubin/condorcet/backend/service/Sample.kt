package com.seanshubin.condorcet.backend.service

class Sample {
    var index = 1
    fun string(): String = (index++).toString()
    fun name(): String = "name-${string()}"
    fun email(): String = "email-${string()}@email.com"
    fun password(): String = "password-${string()}"
    fun uniqueId(): String = "unique-id-${string()}"
    fun hash(s: String) = "hash-for($s)"
}