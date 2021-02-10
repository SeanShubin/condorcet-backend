package com.seanshubin.condorcet.backend.service

class ServiceEnvironmentFactoryImpl(private val service: Service) : ServiceEnvironmentFactory {
    override fun createEnvironment(accessToken: AccessToken?, refreshToken: RefreshToken?): ServiceEnvironment {
        return ServiceEnvironmentImpl(service, accessToken, refreshToken)
    }
}
