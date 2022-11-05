package com.seanshubin.condorcet.backend.service.http

import com.seanshubin.condorcet.backend.service.AccessToken
import com.seanshubin.condorcet.backend.service.RefreshToken
import java.time.Duration

interface TokenUtil {
    fun toAccessTokenString(accessToken: AccessToken, duration: Duration): String
    fun toRefreshTokenString(refreshToken: RefreshToken): String
    fun toRefreshToken(refreshTokenString: String?): RefreshToken?
    fun bearerTokenStringToAccessToken(bearerTokenString: String?): AccessToken?
    fun createUpdatePasswordLink(accessToken: AccessToken, baseUri: String): String
}
