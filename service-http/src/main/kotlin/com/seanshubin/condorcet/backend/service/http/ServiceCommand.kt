package com.seanshubin.condorcet.backend.service.http

import com.auth0.jwt.exceptions.JWTDecodeException
import com.seanshubin.condorcet.backend.domain.Role
import com.seanshubin.condorcet.backend.http.*
import com.seanshubin.condorcet.backend.json.JsonMappers
import com.seanshubin.condorcet.backend.jwt.Cipher
import com.seanshubin.condorcet.backend.service.AccessToken
import com.seanshubin.condorcet.backend.service.RefreshToken
import com.seanshubin.condorcet.backend.service.ServiceException
import com.seanshubin.condorcet.backend.service.Tokens

interface ServiceCommand {
    fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue

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
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue =
            requireAccessToken(request, environment.cipher) { accessToken ->
                environment.service.setRole(accessToken, name, role)
                responseBuilder().build()
            }
    }

    data class RemoveUser(val name: String) : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue =
            requireAccessToken(request, environment.cipher) { accessToken ->
                environment.service.removeUser(accessToken, name)
                responseBuilder().build()
            }
    }

    object ListUsers : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue =
            requireAccessToken(request, environment.cipher) { accessToken ->
                val users = environment.service.listUsers(accessToken)
                val value = mapOf("users" to users)
                responseBuilder().json(value).build()
            }
    }

    data class Unsupported(val name: String, val content: String) : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue {
            return responseBuilder().unsupported("Unsupported command '$name'\n$content").build()
        }
    }

    data class Malformed(val name: String, val content: String) : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue {
            return responseBuilder().unsupported("Malformed body for command '$name'\n$content").build()
        }
    }

    class ServiceExceptionCommand(val serviceException: ServiceException) : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue {
            val status = statusCodeMap[serviceException::class] ?: 500
            return responseBuilder().status(status).userSafeMessage(serviceException.userSafeMessage).build()
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

        fun unsupported(message: String): ResponseBuilder {
            return status(400).userSafeMessage(message)
        }

        fun build(): ResponseValue {
            val status = this.status ?: 200
            val refreshTokenCookieList = createRefreshTokenCookieList(refreshTokenString)
            val contentTypeHeaderList = createContentTypeHeaderList(contentType)
            val headers = HeaderList(contentTypeHeaderList + refreshTokenCookieList.map { it.toHeader() })
            return ResponseValue(status, body, headers)
        }

        private fun createContentTypeHeaderList(contentType: String?): List<Header> =
            if (contentType == null) {
                emptyList()
            } else {
                listOf(createContentTypeHeader(contentType))
            }

        private fun createContentTypeHeader(contentType: String): Header =
            Header("Content-Type", contentType)

        private fun createRefreshTokenCookie(refreshTokenString: String): SetCookie =
            SetCookie("Refresh", refreshTokenString, httpOnly = true)

        private fun createRefreshTokenCookieList(refreshTokenString: String?): List<SetCookie> =
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
            val value = mapOf(
                "accessToken" to accessTokenString,
                "userName" to tokens.accessToken.userName,
                "role" to tokens.accessToken.role.name
            )
            return responseBuilder().refreshToken(refreshTokenString).json(value).build()
        }

        private fun responseBuilder(): ResponseBuilder = ResponseBuilder()

        private val statusCodeMap = mapOf(
            ServiceException.Unauthorized::class to 401,
            ServiceException.NotFound::class to 404,
            ServiceException.Conflict::class to 409,
            ServiceException.Unsupported::class to 400,
            ServiceException.MalformedJson::class to 400
        )

        private fun requireAccessToken(
            request: RequestValue,
            cipher: Cipher,
            f: (AccessToken) -> ResponseValue
        ): ResponseValue {
            return try {
                val accessToken = request.accessToken(cipher)
                if (accessToken == null) {
                    responseBuilder().unauthorized("No valid access token").build()
                } else {
                    f(accessToken)
                }
            } catch (ex: JWTDecodeException) {
                val bearerToken = request.bearerToken() ?: "<null>"
                responseBuilder().unauthorized("Unable to decode access token ($bearerToken): ${ex.message}").build()
            }
        }
    }
}
