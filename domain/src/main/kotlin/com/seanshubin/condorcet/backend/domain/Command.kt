package com.seanshubin.condorcet.backend.domain

interface Command {
    fun exec(service: Service): Response
    data class AddUser(val name: String, val email: String, val password: String) : Command {
        override fun exec(service: Service): Response {
            return service.addUser(name, email, password)
        }
    }

    data class Authenticate(val nameOrEmail: String, val password: String) : Command {
        override fun exec(service: Service): Response {
            return service.authenticate(nameOrEmail, password)
        }
    }

    data class Unsupported(val name: String, val json: String) : Command {
        override fun exec(service: Service): Response {
            return service.unsupported(name, json)
        }
    }

    object Health : Command {
        override fun exec(service: Service): Response {
            return service.health()
        }
    }

    data class MalformedJson(val name: String, val text: String) : Command {
        override fun exec(service: Service): Response {
            val userSafeMessage = "Malformed json for command '$name'\n$text"
            return Response.MalformedJson(userSafeMessage, name, text)
        }
    }
}
