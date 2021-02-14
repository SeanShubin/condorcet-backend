package com.seanshubin.condorcet.backend.service

class ServiceException(val category: Category, val userSafeMessage: String) : RuntimeException(userSafeMessage) {
    enum class Category {
        UNAUTHORIZED,
        NOT_FOUND,
        CONFLICT,
        UNSUPPORTED,
        MALFORMED_JSON
    }
}
