package com.seanshubin.condorcet.backend.service

interface ServiceRequest {
    fun exec(service: Service): ServiceResponse
    data class AddUser(val name: String, val email: String, val password: String) : ServiceRequest {
        override fun exec(service: Service): ServiceResponse {
            return service.addUser(name, email, password)
        }
    }

    data class Authenticate(val nameOrEmail: String, val password: String) : ServiceRequest {
        override fun exec(service: Service): ServiceResponse {
            return service.authenticate(nameOrEmail, password)
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
