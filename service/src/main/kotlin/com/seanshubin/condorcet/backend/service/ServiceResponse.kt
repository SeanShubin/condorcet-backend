package com.seanshubin.condorcet.backend.service

interface ServiceResponse {
    data class UserName(val name: String) : ServiceResponse
    data class Health(val status: String) : ServiceResponse
    data class Unauthorized(val userSafeMessage: String) : ServiceResponse
    data class NotFound(val userSafeMessage: String) : ServiceResponse
    data class Conflict(val userSafeMessage: String) : ServiceResponse
    data class Unsupported(val userSafeMessage: String, val name: String, val text: String) : ServiceResponse
    data class MalformedJson(val userSafeMessage: String, val name: String, val text: String) : ServiceResponse
    object GenericOk : ServiceResponse
}
