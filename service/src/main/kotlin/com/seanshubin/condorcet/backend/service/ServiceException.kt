package com.seanshubin.condorcet.backend.service

abstract class ServiceException(val userSafeMessage: String) : RuntimeException(userSafeMessage) {
    class Unauthorized(userSafeMessage: String) : ServiceException(userSafeMessage)
    class NotFound(userSafeMessage: String) : ServiceException(userSafeMessage)
    class Conflict(userSafeMessage: String) : ServiceException(userSafeMessage)
    class Unsupported(userSafeMessage: String) : ServiceException(userSafeMessage)
    class MalformedJson(userSafeMessage: String) : ServiceException(userSafeMessage)
}
