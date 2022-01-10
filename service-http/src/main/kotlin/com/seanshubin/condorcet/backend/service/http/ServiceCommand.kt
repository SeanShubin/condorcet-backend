package com.seanshubin.condorcet.backend.service.http

import com.auth0.jwt.exceptions.JWTDecodeException
import com.seanshubin.condorcet.backend.domain.ElectionUpdates
import com.seanshubin.condorcet.backend.domain.Ranking
import com.seanshubin.condorcet.backend.domain.Role
import com.seanshubin.condorcet.backend.http.*
import com.seanshubin.condorcet.backend.json.JsonMappers
import com.seanshubin.condorcet.backend.jwt.Cipher
import com.seanshubin.condorcet.backend.service.AccessToken
import com.seanshubin.condorcet.backend.service.RefreshToken
import com.seanshubin.condorcet.backend.service.ServiceException
import com.seanshubin.condorcet.backend.service.ServiceException.Category.*
import com.seanshubin.condorcet.backend.service.Tokens
import java.time.Instant

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

        override fun toString(): String = "Refresh"
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

    object Logout : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue {
            return responseBuilder().clearRefreshToken().build()
        }

        override fun toString(): String = "Logout"
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

    data class AddElection(val name: String) : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue =
            requireAccessToken(request, environment.cipher) { accessToken ->
                environment.service.addElection(accessToken, name)
                responseBuilder().build()
            }
    }

    data class LaunchElection(val name: String, val allowEdit:Boolean) : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue =
            requireAccessToken(request, environment.cipher) { accessToken ->
                environment.service.launchElection(accessToken, name, allowEdit)
                responseBuilder().build()
            }
    }

    data class FinalizeElection(val name: String) : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue =
            requireAccessToken(request, environment.cipher) { accessToken ->
                environment.service.finalizeElection(accessToken, name)
                responseBuilder().build()
            }
    }

    data class UpdateElection(
        val name: String,
        val newName: String?,
        val secretBallot: Boolean?,
        val clearNoVotingBefore: Boolean,
        val noVotingBefore: Instant?,
        val clearNoVotingAfter: Boolean,
        val noVotingAfter: Instant?,
        val restrictWhoCanVote: Boolean?,
        val ownerCanDeleteBallots: Boolean?,
        val auditorCanDeleteBallots: Boolean?,
        val isTemplate: Boolean?
    ) : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue =
            requireAccessToken(request, environment.cipher) { accessToken ->
                val electionConfig = toElectionConfig()
                environment.service.updateElection(accessToken, name, electionConfig)
                responseBuilder().build()
            }
    }

    data class GetElection(val name: String) : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue =
            requireAccessToken(request, environment.cipher) { accessToken ->
                val electionAndCanUpdate = environment.service.getElection(accessToken, name)
                responseBuilder().json(electionAndCanUpdate).build()
            }
    }

    data class DeleteElection(val name: String) : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue =
            requireAccessToken(request, environment.cipher) { accessToken ->
                environment.service.deleteElection(accessToken, name)
                responseBuilder().build()
            }
    }

    object ListElections : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue =
            requireAccessToken(request, environment.cipher) { accessToken ->
                val elections = environment.service.listElections(accessToken)
                val values = mapOf("elections" to elections)
                responseBuilder().json(values).build()
            }

        override fun toString(): String = "ListElections"
    }

    object ListUsers : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue =
            requireAccessToken(request, environment.cipher) { accessToken ->
                val users = environment.service.listUsers(accessToken)
                val value = mapOf("users" to users)
                responseBuilder().json(value).build()
            }

        override fun toString(): String = "ListUsers"
    }

    object UserCount : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue =
            requireAccessToken(request, environment.cipher) { accessToken ->
                val userCount = environment.service.userCount(accessToken)
                val value = mapOf("userCount" to userCount)
                responseBuilder().json(value).build()
            }

        override fun toString(): String = "UserCount"
    }

    object ElectionCount : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue =
            requireAccessToken(request, environment.cipher) { accessToken ->
                val userCount = environment.service.electionCount(accessToken)
                val value = mapOf("electionCount" to userCount)
                responseBuilder().json(value).build()
            }

        override fun toString(): String = "ElectionCount"
    }

    object TableCount : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue =
            requireAccessToken(request, environment.cipher) { accessToken ->
                val userCount = environment.service.tableCount(accessToken)
                val value = mapOf("tableCount" to userCount)
                responseBuilder().json(value).build()
            }

        override fun toString(): String = "TableCount"
    }

    object EventCount : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue =
            requireAccessToken(request, environment.cipher) { accessToken ->
                val userCount = environment.service.eventCount(accessToken)
                val value = mapOf("eventCount" to userCount)
                responseBuilder().json(value).build()
            }

        override fun toString(): String = "EventCount"
    }

    object ListTables : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue =
            requireAccessToken(request, environment.cipher) { accessToken ->
                val tables = environment.service.listTables(accessToken)
                val value = mapOf("tableNames" to tables)
                responseBuilder().json(value).build()
            }

        override fun toString(): String = "ListTables"
    }

    data class TableData(val name: String) : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue =
            requireAccessToken(request, environment.cipher) { accessToken ->
                val tableData = environment.service.tableData(accessToken, name)
                val value = mapOf("table" to tableData)
                responseBuilder().json(value).build()
            }
    }

    data class DebugTableData(val name: String) : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue =
            requireAccessToken(request, environment.cipher) { accessToken ->
                val tableData = environment.service.debugTableData(accessToken, name)
                val value = mapOf("table" to tableData)
                responseBuilder().json(value).build()
            }
    }

    object EventData : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue =
            requireAccessToken(request, environment.cipher) { accessToken ->
                val eventData = environment.service.eventData(accessToken)
                val value = mapOf("events" to eventData)
                responseBuilder().json(value).build()
            }

        override fun toString(): String = "EventData"
    }

    data class SetCandidates(val electionName: String, val candidateNames: List<String>) : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue =
            requireAccessToken(request, environment.cipher) { accessToken ->
                environment.service.setCandidates(accessToken, electionName, candidateNames)
                responseBuilder().build()
            }
    }

    data class ListCandidates(val electionName: String) : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue =
            requireAccessToken(request, environment.cipher) { accessToken ->
                val candidateNames = environment.service.listCandidates(accessToken, electionName)
                val value = mapOf("candidates" to candidateNames)
                responseBuilder().json(value).build()
            }
    }

    data class CastBallot(val voterName: String, val electionName: String, val rankings: List<Ranking>) :
        ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue =
            requireAccessToken(request, environment.cipher) { accessToken ->
                environment.service.castBallot(accessToken, voterName, electionName, rankings)
                responseBuilder().build()
            }
    }

    data class ListRankings(val voterName: String, val electionName: String) : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue =
            requireAccessToken(request, environment.cipher) { accessToken ->
                val elections = environment.service.listRankings(accessToken, voterName, electionName)
                val values = mapOf("rankings" to elections)
                responseBuilder().json(values).build()
            }
    }

    data class Tally(val electionName: String) : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue =
            requireAccessToken(request, environment.cipher) { accessToken ->
                val tally = environment.service.tally(accessToken, electionName)
                responseBuilder().json(tally).build()
            }
    }

    object ListVoterNames : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue =
            requireAccessToken(request, environment.cipher) { accessToken ->
                val voterNames = environment.service.listVoterNames(accessToken)
                responseBuilder().json(voterNames).build()
            }

        override fun toString(): String = "ListUserNames"
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
            val status = statusCodeMap[serviceException.category] ?: 500
            return responseBuilder().status(status).userSafeMessage(serviceException.userSafeMessage).build()
        }
    }

    class TopLevelExceptionCommand(val exception: Throwable) : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue {
            val status = 500
            val userSafeMessage = "server error: ${exception.message}"
            return responseBuilder().status(status).userSafeMessage(userSafeMessage).build()
        }
    }

    data class ResponseBuilder(
        val status: Int? = null,
        val refreshTokenString: String? = null,
        val contentType: String? = null,
        val body: String? = null,
        val shouldClearRefreshToken: Boolean = false
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

        fun clearRefreshToken(): ResponseBuilder {
            return copy(shouldClearRefreshToken = true)
        }

        fun build(): ResponseValue {
            val status = this.status ?: 200
            val refreshTokenCookieList = if (shouldClearRefreshToken) {
                createRefreshTokenCookieList("")
            } else {
                createRefreshTokenCookieList(refreshTokenString)
            }
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
        fun UpdateElection.toElectionConfig(): ElectionUpdates =
            ElectionUpdates(
                newName,
                secretBallot,
                clearNoVotingBefore,
                noVotingBefore,
                clearNoVotingAfter,
                noVotingAfter,
                restrictWhoCanVote,
                ownerCanDeleteBallots,
                auditorCanDeleteBallots,
                isTemplate
            )

        private fun RequestValue.refreshToken(cipher: Cipher): RefreshToken? {
            val refreshTokenString = cookieValue("Refresh") ?: return null
            if (refreshTokenString.isBlank()) return null
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
            UNAUTHORIZED to 401,
            NOT_FOUND to 404,
            CONFLICT to 409,
            UNSUPPORTED to 400,
            UNSUPPORTED to 400
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
