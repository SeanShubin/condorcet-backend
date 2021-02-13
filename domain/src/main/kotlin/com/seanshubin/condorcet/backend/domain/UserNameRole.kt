package com.seanshubin.condorcet.backend.domain

data class UserNameRole(val name: String, val role: Role, val allowedRoles: List<Role>)
