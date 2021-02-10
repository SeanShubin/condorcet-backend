package com.seanshubin.condorcet.backend.service

interface ServiceEnvironmentFactory {
    fun createEnvironment(
        accessToken: AccessToken?,
        refreshToken: RefreshToken?
    ): ServiceEnvironment
}
