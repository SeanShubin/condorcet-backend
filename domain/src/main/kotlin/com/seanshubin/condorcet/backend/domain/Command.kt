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
}
