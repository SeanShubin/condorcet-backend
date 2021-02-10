package com.seanshubin.condorcet.backend.server

import com.seanshubin.condorcet.backend.service.AccessToken
import com.seanshubin.condorcet.backend.service.RefreshToken
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

interface TokenService {
    fun getAccessToken(request: HttpServletRequest): AccessToken?
    fun getRefreshToken(request: HttpServletRequest): RefreshToken?
    fun setRefreshToken(response: HttpServletResponse, refreshToken: RefreshToken?)
}
