package com.seanshubin.condorcet.backend.service.http

import com.seanshubin.condorcet.backend.domain.Role
import com.seanshubin.condorcet.backend.global.constants.Constants
import com.seanshubin.condorcet.backend.jwt.Cipher
import com.seanshubin.condorcet.backend.service.AccessToken
import com.seanshubin.condorcet.backend.service.RefreshToken
import java.time.Duration

class TokenUtilImpl(
    private val lookupHost: () -> String,
    private val cipher: Cipher
) : TokenUtil {
    override fun toRefreshTokenString(refreshToken: RefreshToken): String =
        cipher.encode(mapOf("userName" to refreshToken.userName), Constants.refreshTokenDuration)

    override fun toAccessTokenString(accessToken: AccessToken, duration: Duration): String =
        cipher.encode(
            mapOf(
                "userName" to accessToken.userName,
                "role" to accessToken.role.name
            ),
            duration
        )

    override fun createUpdatePasswordLink(accessToken: AccessToken): String {
        val host = lookupHost()
        val accessTokenString = toAccessTokenString(accessToken, Constants.emailAccessTokenDuration)
        val link = "$host/changePassword?accessToken=$accessTokenString"
        return link
    }

    override fun toRefreshToken(refreshTokenString: String?): RefreshToken? {
        if (refreshTokenString == null) return null
        if (refreshTokenString.isBlank()) return null
        val decoded = cipher.decode(refreshTokenString)
        val userName = decoded["userName"] ?: return null
        return RefreshToken(userName)
    }

    override fun bearerTokenStringToAccessToken(bearerTokenString: String?): AccessToken? {
        if (bearerTokenString == null) return null
        val decoded = cipher.decode(bearerTokenString)
        val userName = decoded["userName"] ?: return null
        val roleName = decoded["role"] ?: return null
        val role = Role.valueOf(roleName)
        return AccessToken(userName, role)
    }
}
