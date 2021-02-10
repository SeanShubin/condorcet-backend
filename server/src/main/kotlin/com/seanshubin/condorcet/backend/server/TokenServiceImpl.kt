package com.seanshubin.condorcet.backend.server

import com.fasterxml.jackson.module.kotlin.readValue
import com.seanshubin.condorcet.backend.json.JsonMappers
import com.seanshubin.condorcet.backend.service.AccessToken
import com.seanshubin.condorcet.backend.service.RefreshToken
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

class TokenServiceImpl : TokenService {
    override fun getAccessToken(request: HttpServletRequest): AccessToken? {
        val headerValue = searchHeaderValue(request, "Authorization") ?: return null
        val bearerToken = parseBearerToken(headerValue) ?: return null
        return composeAccessToken(bearerToken)
    }

    override fun getRefreshToken(request: HttpServletRequest): RefreshToken? {
        val cookies = request.cookies ?: return null
        val cookie = cookies.toList().find { it.name == "Refresh" } ?: return null
        return composeRefreshToken(cookie.value)
    }

    override fun setRefreshToken(response: HttpServletResponse, refreshToken: RefreshToken?) {
        if (refreshToken == null) return
        val cookie = Cookie("Refresh", JsonMappers.compact.writeValueAsString(refreshToken))
        cookie.isHttpOnly = true
        response.addCookie(cookie)
    }

    private fun searchHeaderValue(request: HttpServletRequest, name: String): String? {
        return request.getHeader(name)
    }

    private fun parseBearerToken(headerValue: String): String? {
        val matchResult = bearerTokenRegex.matchEntire(headerValue) ?: return null
        return matchResult.groupValues[1]
    }

    private fun composeAccessToken(bearerToken: String): AccessToken {
        return JsonMappers.parser.readValue(bearerToken)
    }

    private fun composeRefreshToken(value: String): RefreshToken {
        return JsonMappers.parser.readValue(value)
    }

    companion object {
        private val bearerTokenRegex = Regex("""^ *Bearer +(.*) *$""")
    }
}
