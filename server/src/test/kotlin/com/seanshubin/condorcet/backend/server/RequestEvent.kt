package com.seanshubin.condorcet.backend.server

import com.seanshubin.condorcet.backend.service.AccessToken
import com.seanshubin.condorcet.backend.service.ServiceRequest

data class RequestEvent(
    val serviceRequest: ServiceRequest,
    val accessToken: AccessToken? = null
)
