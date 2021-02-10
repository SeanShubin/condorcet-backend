package com.seanshubin.condorcet.backend.service

data class ServiceResponse(
    val refreshToken: RefreshToken?,
    val value: Any?
)
