package com.seanshubin.condorcet.backend.domain

interface ServiceEvent {
    fun exec(service: Service): ServiceResponse
    data class AddUser(val name: String, val email: String, val password: String) : ServiceEvent {
        override fun exec(service: Service): ServiceResponse {
            return service.addUser(name, email, password)
        }
    }

    data class Authenticate(val nameOrEmail: String, val password: String) : ServiceEvent {
        override fun exec(service: Service): ServiceResponse {
            return service.authenticate(nameOrEmail, password)
        }
    }

    data class Unsupported(val name: String, val json: String) : ServiceEvent {
        override fun exec(service: Service): ServiceResponse {
            return service.unsupported(name, json)
        }
    }

    object Health : ServiceEvent {
        override fun exec(service: Service): ServiceResponse {
            return service.health()
        }
    }

    data class MalformedJson(val name: String, val text: String) : ServiceEvent {
        override fun exec(service: Service): ServiceResponse {
            val userSafeMessage = "Malformed json for command '$name'\n$text"
            return ServiceResponse.MalformedJson(userSafeMessage, name, text)
        }
    }
}
