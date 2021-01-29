package com.seanshubin.condorcet.backend.domain

interface Service {
    fun addUser(name: String, email: String, password: String): Response
    fun authenticate(name: String, password: String): Response
    fun health(): Response
    fun unsupported(name: String, text: String): Response
}
