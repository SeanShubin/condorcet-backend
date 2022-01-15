package com.seanshubin.condorcet.backend.domain

data class UserNameRole(val userName: String, val role: Role, val allowedRoles: List<Role>)
