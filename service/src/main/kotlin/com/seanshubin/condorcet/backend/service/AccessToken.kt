package com.seanshubin.condorcet.backend.service

import com.seanshubin.condorcet.backend.domain.Role

data class AccessToken(val userName: String, val role: Role)
