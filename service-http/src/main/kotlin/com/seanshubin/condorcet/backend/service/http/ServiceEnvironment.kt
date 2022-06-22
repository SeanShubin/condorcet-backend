package com.seanshubin.condorcet.backend.service.http

import com.seanshubin.condorcet.backend.service.Service

class ServiceEnvironment(
    val service: Service,
    val tokenUtil: TokenUtil,
    val topLevelException: (Throwable) -> Unit
)
