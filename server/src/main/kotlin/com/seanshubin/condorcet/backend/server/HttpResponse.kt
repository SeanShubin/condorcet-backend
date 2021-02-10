package com.seanshubin.condorcet.backend.server

import com.seanshubin.condorcet.backend.service.RefreshToken

data class HttpResponse(
    val status: Int,
    val value: Any?,
    val refreshToken: RefreshToken?
)
