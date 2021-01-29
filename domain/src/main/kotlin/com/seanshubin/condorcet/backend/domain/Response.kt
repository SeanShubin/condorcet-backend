package com.seanshubin.condorcet.backend.domain

interface Response {
    data class Unauthorized(val userSafeMessage: String) : Response
    data class NotFound(val userSafeMessage: String) : Response
    data class Conflict(val userSafeMessage: String) : Response
    data class UserName(val name: String) : Response
    data class Health(val status: String) : Response
    data class Unsupported(val userSafeMessage: String, val name: String, val text: String) : Response
}
