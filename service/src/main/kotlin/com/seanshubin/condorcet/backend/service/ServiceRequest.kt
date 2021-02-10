package com.seanshubin.condorcet.backend.service

import com.seanshubin.condorcet.backend.domain.Role

interface ServiceRequest {
    fun exec(service: Service, accessToken: AccessToken?, refreshToken: RefreshToken?): ServiceResponse

    object Refresh : ServiceRequest {
        override fun exec(service: Service, accessToken: AccessToken?, refreshToken: RefreshToken?): ServiceResponse {
            if (refreshToken == null) {
                throw ServiceException.Unauthorized("No valid refresh token")
            } else {
                val tokens = service.refresh(refreshToken)
                return ServiceResponse(
                    refreshToken = tokens.refreshToken,
                    value = tokens.accessToken
                )
            }
        }
    }

    data class Register(val name: String, val email: String, val password: String) : ServiceRequest {
        override fun exec(service: Service, accessToken: AccessToken?, refreshToken: RefreshToken?): ServiceResponse {
            val tokens = service.register(name, email, password)
            return ServiceResponse(
                refreshToken = tokens.refreshToken,
                value = tokens.accessToken
            )
        }
    }

    data class Authenticate(val nameOrEmail: String, val password: String) : ServiceRequest {
        override fun exec(service: Service, accessToken: AccessToken?, refreshToken: RefreshToken?): ServiceResponse {
            val tokens = service.authenticate(nameOrEmail, password)
            return ServiceResponse(
                refreshToken = tokens.refreshToken,
                value = tokens.accessToken
            )
        }
    }

    data class SetRole(val name: String, val role: Role) : ServiceRequest {
        override fun exec(service: Service, accessToken: AccessToken?, refreshToken: RefreshToken?): ServiceResponse {
            if (accessToken == null) {
                throw ServiceException.Unauthorized("No valid access token")
            } else {
                service.setRole(accessToken, name, role)
                return ServiceResponse(
                    refreshToken = null,
                    value = null
                )
            }
        }
    }

    data class RemoveUser(val name: String) : ServiceRequest {
        override fun exec(service: Service, accessToken: AccessToken?, refreshToken: RefreshToken?): ServiceResponse {
            if (accessToken == null) {
                throw ServiceException.Unauthorized("No valid access token")
            } else {
                val value = service.removeUser(accessToken, name)
                return ServiceResponse(
                    refreshToken = null,
                    value = value
                )
            }
        }
    }

    object ListUsers : ServiceRequest {
        override fun exec(service: Service, accessToken: AccessToken?, refreshToken: RefreshToken?): ServiceResponse {
            if (accessToken == null) {
                throw ServiceException.Unauthorized("No valid access token")
            } else {
                val value = service.listUsers(accessToken)
                return ServiceResponse(
                    refreshToken = null,
                    value = value
                )
            }
        }
    }
}
