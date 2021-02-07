package com.seanshubin.condorcet.backend.service

import com.seanshubin.condorcet.backend.domain.Role

interface Service {
    fun addUser(name: String, email: String, password: String): ServiceResponse
    fun authenticate(name: String, password: String): ServiceResponse
    fun setRole(authority: String, name: String, role: Role): ServiceResponse
    fun health(): ServiceResponse
    fun unsupported(name: String, text: String): ServiceResponse
}
