package com.seanshubin.condorcet.backend.service

interface ServiceEnvironment {
    val service: Service
    val accessToken: AccessToken?
    val refreshToken: RefreshToken?
}