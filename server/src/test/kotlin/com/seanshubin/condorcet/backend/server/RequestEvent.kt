package com.seanshubin.condorcet.backend.server

import com.seanshubin.condorcet.backend.service.AccessToken
import com.seanshubin.condorcet.backend.service.http.ServiceCommand

data class RequestEvent(
    val serviceCommand: ServiceCommand,
    val accessToken: AccessToken? = null
)
