package com.seanshubin.condorcet.backend.service

import com.seanshubin.condorcet.backend.domain.Role

interface ServiceRequest {
    fun exec(service: Service): ServiceResponse
    data class Register(val name: String, val email: String, val password: String) : ServiceRequest {
        override fun exec(service: Service): ServiceResponse {
            return service.register(name, email, password)
        }
    }

    data class Authenticate(val nameOrEmail: String, val password: String) : ServiceRequest {
        override fun exec(service: Service): ServiceResponse {
            return service.authenticate(nameOrEmail, password)
        }
    }

    data class SetRole(val authority: String, val name: String, val role: Role) : ServiceRequest {
        override fun exec(service: Service): ServiceResponse {
            return service.setRole(authority, name, role)
        }
    }

    data class RemoveUser(val authority: String, val name: String) : ServiceRequest {
        override fun exec(service: Service): ServiceResponse {
            return service.removeUser(authority, name)
        }
    }

    data class ListUsers(val authority: String) : ServiceRequest {
        override fun exec(service: Service): ServiceResponse {
            return service.listUsers(authority)
        }
    }

    data class Unsupported(val name: String, val json: String) : ServiceRequest {
        override fun exec(service: Service): ServiceResponse {
            return service.unsupported(name, json)
        }
    }

    object Health : ServiceRequest {
        override fun exec(service: Service): ServiceResponse {
            return service.health()
        }
    }

    data class MalformedJson(val name: String, val text: String) : ServiceRequest {
        override fun exec(service: Service): ServiceResponse {
            val userSafeMessage = "Malformed json for command '$name'\n$text"
            return ServiceResponse.MalformedJson(userSafeMessage, name, text)
        }
    }
}
