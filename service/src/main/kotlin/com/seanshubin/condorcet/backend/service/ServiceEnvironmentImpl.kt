package com.seanshubin.condorcet.backend.service

class ServiceEnvironmentImpl(
    override val service: Service,
    override val accessToken: AccessToken?,
    override val refreshToken: RefreshToken?
) : ServiceEnvironment
