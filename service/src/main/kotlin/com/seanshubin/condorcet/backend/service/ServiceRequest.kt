package com.seanshubin.condorcet.backend.service

import com.seanshubin.condorcet.backend.domain.Role

interface ServiceRequest {
    fun exec(service: Service): Any
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

    data class SetRole(val authority: String, val name: String, val role: Role) : ServiceRequest {
        override fun exec(service: Service): Any {
            return service.setRole(authority, name, role)
        }
    }

    data class RemoveUser(val authority: String, val name: String) : ServiceRequest {
        override fun exec(service: Service): Any {
            return service.removeUser(authority, name)
        }
    }

    data class ListUsers(val authority: String) : ServiceRequest {
        override fun exec(service: Service): Any {
            return service.listUsers(authority)
        }
    }
}
