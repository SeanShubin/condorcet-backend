package com.seanshubin.condorcet.backend.service

import com.seanshubin.condorcet.backend.domain.Role

interface ServiceRequest {
    fun exec(service: Service): Any
    data class Refresh(val refreshToken: RefreshToken) : ServiceRequest {
        override fun exec(service: Service): Any {
            return service.refresh(refreshToken)
        }
    }

    data class Register(val name: String, val email: String, val password: String) : ServiceRequest {
        override fun exec(service: Service): Any {
            return service.register(name, email, password)
        }
    }

    data class Authenticate(val nameOrEmail: String, val password: String) : ServiceRequest {
        override fun exec(service: Service): Any {
            return service.authenticate(nameOrEmail, password)
        }
    }

    data class SetRole(val accessToken: AccessToken, val name: String, val role: Role) : ServiceRequest {
        override fun exec(service: Service): Any {
            return service.setRole(accessToken, name, role)
        }
    }

    data class RemoveUser(val accessToken: AccessToken, val name: String) : ServiceRequest {
        override fun exec(service: Service): Any {
            return service.removeUser(accessToken, name)
        }
    }

    data class ListUsers(val accessToken: AccessToken) : ServiceRequest {
        override fun exec(service: Service): Any {
            return service.listUsers(accessToken)
        }
    }
}
