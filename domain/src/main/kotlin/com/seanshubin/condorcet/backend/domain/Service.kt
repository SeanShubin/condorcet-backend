package com.seanshubin.condorcet.backend.domain

interface Service {
    fun addUser(name: String, email: String, password: String): ServiceResponse
    fun authenticate(name: String, password: String): ServiceResponse
    fun health(): ServiceResponse
    fun unsupported(name: String, text: String): ServiceResponse
}
