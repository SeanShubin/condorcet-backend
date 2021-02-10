package com.seanshubin.condorcet.backend.service.http

import com.seanshubin.condorcet.backend.domain.Role
import com.seanshubin.condorcet.backend.http.*
import com.seanshubin.condorcet.backend.json.JsonMappers
import com.seanshubin.condorcet.backend.jwt.Cipher
import com.seanshubin.condorcet.backend.service.AccessToken
import com.seanshubin.condorcet.backend.service.RefreshToken
import com.seanshubin.condorcet.backend.service.Tokens

interface ServiceCommand {
    fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue
    fun toHttpCommand(environment: ServiceEnvironment): HttpCommand {
        return object : HttpCommand {
            override fun exec(request: RequestValue): ResponseValue {
                return exec(environment, request)
            }
        }
    }

    object Refresh : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue {
            val refreshToken = request.refreshToken(environment.cipher)
            return if (refreshToken == null) {
                responseBuilder().unauthorized("No valid refresh token").build()
            } else {
                val tokens = environment.service.refresh(refreshToken)
                tokenResponse(tokens, environment.cipher)
            }
        }
    }

    data class Register(val name: String, val email: String, val password: String) : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue {
            val tokens = environment.service.register(name, email, password)
            return tokenResponse(tokens, environment.cipher)
        }
    }

    data class Authenticate(val nameOrEmail: String, val password: String) : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue {
            val tokens = environment.service.authenticate(nameOrEmail, password)
            return tokenResponse(tokens, environment.cipher)
        }
    }

    data class SetRole(val name: String, val role: Role) : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue {
            val accessToken = request.accessToken(environment.cipher)
            return if (accessToken == null) {
                responseBuilder().unauthorized("No valid access token").build()
            } else {
                environment.service.setRole(accessToken, name, role)
                return responseBuilder().build()
            }
        }
    }

    data class RemoveUser(val name: String) : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue {
            val accessToken = request.accessToken(environment.cipher)
            return if (accessToken == null) {
                responseBuilder().unauthorized("No valid access token").build()
            } else {
                val value = environment.service.removeUser(accessToken, name)
                return responseBuilder().json(value).build()
            }
        }
    }

    object ListUsers : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue {
            val accessToken = request.accessToken(environment.cipher)
            return if (accessToken == null) {
                responseBuilder().unauthorized("No valid access token").build()
            } else {
                val value = environment.service.listUsers(accessToken)
                return responseBuilder().json(value).build()
            }
        }
    }


    data class ResponseBuilder(
        val status: Int? = null,
        val refreshTokenString: String? = null,
        val contentType: String? = null,
        val body: String? = null
    ) {
        fun refreshToken(refreshTokenString: String): ResponseBuilder {
            if (this.refreshTokenString != null) throw RuntimeException("refreshToken already set")
            return copy(refreshTokenString = refreshTokenString)
        }

        fun status(status: Int): ResponseBuilder {
            if (this.status != null) throw RuntimeException("status already set")
            return copy(status = status)
        }

        fun unauthorized(message: String): ResponseBuilder {
            return status(401).userSafeMessage(message)
        }

        fun build(): ResponseValue {
            val status = this.status ?: 200
            val refreshTokenCookieList = createRefreshTokenCookieList(refreshTokenString)
            val contentTypeHeaderList = createContentTypeHeaderList(contentType)
            val headers = HeaderList(contentTypeHeaderList + refreshTokenCookieList.map { it.toHeader() })
            return ResponseValue(status, body, headers)
        }

        fun createContentTypeHeaderList(contentType: String?): List<Header> =
            if (contentType == null) {
                emptyList()
            } else {
                listOf(createContentTypeHeader(contentType))
            }

        fun createContentTypeHeader(contentType: String): Header =
            Header("Content-Type", contentType)

        fun createRefreshTokenCookie(refreshTokenString: String): SetCookie =
            SetCookie("Refresh", refreshTokenString, httpOnly = true)

        fun createRefreshTokenCookieList(refreshTokenString: String?): List<SetCookie> =
            if (refreshTokenString == null) {
                emptyList()
            } else {
                listOf(createRefreshTokenCookie(refreshTokenString))
            }

        fun json(value: Any?): ResponseBuilder {
            if (this.contentType != null) throw RuntimeException("contentType already set")
            if (this.body != null) throw RuntimeException("body already set")
            val contentType = "application/json"
            val body = JsonMappers.pretty.writeValueAsString(value)
            return copy(contentType = contentType, body = body)
        }

        fun userSafeMessage(message: String): ResponseBuilder =
            json(mapOf("userSafeMessage" to message))
    }

    companion object {
        private fun RequestValue.refreshToken(cipher: Cipher): RefreshToken? {
            val refreshTokenString = cookieValue("Refresh") ?: return null
            val decoded = cipher.decode(refreshTokenString)
            val userName = decoded.claims["userName"]?.asString() ?: return null
            return RefreshToken(userName)

        }

        private fun RequestValue.accessToken(cipher: Cipher): AccessToken? {
            val bearerToken = bearerToken() ?: return null
            val decoded = cipher.decode(bearerToken)
            val userName = decoded.claims["userName"]?.asString() ?: return null
            val roleName = decoded.claims["role"]?.asString() ?: return null
            val role = Role.valueOf(roleName)
            return AccessToken(userName, role)
        }

        private fun tokenResponse(tokens: Tokens, cipher: Cipher): ResponseValue {
            val refreshTokenString = cipher.encode(mapOf("userName" to tokens.refreshToken.userName))
            val accessTokenString = cipher.encode(
                mapOf(
                    "userName" to tokens.accessToken.userName,
                    "role" to tokens.accessToken.role.name
                )
            )
            val accessTokenObject = mapOf("accessToken" to accessTokenString)
            return responseBuilder().refreshToken(refreshTokenString).json(accessTokenObject).build()
        }

        private fun responseBuilder(): ResponseBuilder = ResponseBuilder()
    }
}
