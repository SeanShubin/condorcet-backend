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

    data class Register(val userName: String, val email: String, val password: String) : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue {
            val tokens = environment.service.register(userName, email, password)
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

    data class SetRole(val userName: String, val role: Role) : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue =
            requireAccessToken(request, environment.cipher) { accessToken ->
                environment.service.setRole(accessToken, userName, role)
                responseBuilder().build()
            }
    }

    data class RemoveUser(val userName: String) : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue =
            requireAccessToken(request, environment.cipher) { accessToken ->
                environment.service.removeUser(accessToken, userName)
                responseBuilder().build()
            }
    }

    data class AddElection(val electionName: String) : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue =
            requireAccessToken(request, environment.cipher) { accessToken ->
                environment.service.addElection(accessToken, electionName)
                responseBuilder().build()
            }
    }

    data class LaunchElection(val electionName: String, val allowEdit: Boolean) : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue =
            requireAccessToken(request, environment.cipher) { accessToken ->
                environment.service.launchElection(accessToken, electionName, allowEdit)
                responseBuilder().build()
            }
    }

    data class FinalizeElection(val electionName: String) : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue =
            requireAccessToken(request, environment.cipher) { accessToken ->
                environment.service.finalizeElection(accessToken, electionName)
                responseBuilder().build()
            }
    }

    data class UpdateElection(
        val electionName: String,
        val newElectionName: String?,
        val secretBallot: Boolean?,
        val clearNoVotingBefore: Boolean,
        val noVotingBefore: Instant?,
        val clearNoVotingAfter: Boolean,
        val noVotingAfter: Instant?
    ) : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue =
            requireAccessToken(request, environment.cipher) { accessToken ->
                val electionConfig = toElectionConfig()
                environment.service.updateElection(accessToken, electionName, electionConfig)
                responseBuilder().build()
            }
    }

    data class GetElection(val electionName: String) : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue =
            requireAccessToken(request, environment.cipher) { accessToken ->
                val election = environment.service.getElection(accessToken, electionName)
                responseBuilder().json(election).build()
            }
    }

    data class DeleteElection(val electionName: String) : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue =
            requireAccessToken(request, environment.cipher) { accessToken ->
                environment.service.deleteElection(accessToken, electionName)
                responseBuilder().build()
            }
    }

    object ListElections : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue =
            requireAccessToken(request, environment.cipher) { accessToken ->
                val elections = environment.service.listElections(accessToken)
                responseBuilder().json(elections).build()
            }

        override fun toString(): String = "ListElections"
    }

    object ListUsers : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue =
            requireAccessToken(request, environment.cipher) { accessToken ->
                val users = environment.service.listUsers(accessToken)
                responseBuilder().json(users).build()
            }

        override fun toString(): String = "ListUsers"
    }

    object UserCount : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue =
            requireAccessToken(request, environment.cipher) { accessToken ->
                val userCount = environment.service.userCount(accessToken)
                responseBuilder().json(userCount).build()
            }

        override fun toString(): String = "UserCount"
    }

    object ElectionCount : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue =
            requireAccessToken(request, environment.cipher) { accessToken ->
                val electionCount = environment.service.electionCount(accessToken)
                responseBuilder().json(electionCount).build()
            }

        override fun toString(): String = "ElectionCount"
    }

    object TableCount : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue =
            requireAccessToken(request, environment.cipher) { accessToken ->
                val tableCount = environment.service.tableCount(accessToken)
                responseBuilder().json(tableCount).build()
            }

        override fun toString(): String = "TableCount"
    }

    object EventCount : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue =
            requireAccessToken(request, environment.cipher) { accessToken ->
                val eventCount = environment.service.eventCount(accessToken)
                responseBuilder().json(eventCount).build()
            }

        override fun toString(): String = "EventCount"
    }

    object ListTables : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue =
            requireAccessToken(request, environment.cipher) { accessToken ->
                val tables = environment.service.listTables(accessToken)
                responseBuilder().json(tables).build()
            }

        override fun toString(): String = "ListTables"
    }

    data class TableData(val tableName: String) : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue =
            requireAccessToken(request, environment.cipher) { accessToken ->
                val tableData = environment.service.tableData(accessToken, tableName)
                responseBuilder().json(tableData).build()
            }
    }

    data class DebugTableData(val tableName: String) : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue =
            requireAccessToken(request, environment.cipher) { accessToken ->
                val tableData = environment.service.debugTableData(accessToken, tableName)
                responseBuilder().json(tableData).build()
            }
    }

    object EventData : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue =
            requireAccessToken(request, environment.cipher) { accessToken ->
                val eventData = environment.service.eventData(accessToken)
                responseBuilder().json(eventData).build()
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
                responseBuilder().json(candidateNames).build()
            }
    }

    data class SetEligibleVoters(val electionName: String, val userNames: List<String>) : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue =
            requireAccessToken(request, environment.cipher) { accessToken ->
                environment.service.setEligibleVoters(accessToken, electionName, userNames)
                responseBuilder().build()
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
                val rankings = environment.service.listRankings(accessToken, voterName, electionName)
                responseBuilder().json(rankings).build()
            }
    }

    data class Tally(val electionName: String) : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue =
            requireAccessToken(request, environment.cipher) { accessToken ->
                val tally = environment.service.tally(accessToken, electionName)
                responseBuilder().json(tally).build()
            }
    }

    data class GetBallot(val voterName:String, val electionName: String) : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue =
            requireAccessToken(request, environment.cipher) { accessToken ->
                val ballot = environment.service.getBallot(accessToken, voterName, electionName)
                responseBuilder().json(ballot).build()
            }
    }

    data class ListEligibility(val electionName: String) : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue =
            requireAccessToken(request, environment.cipher) { accessToken ->
                val voterNames = environment.service.listEligibility(accessToken, electionName)
                responseBuilder().json(voterNames).build()
            }
    }

    data class IsEligible(val userName: String, val electionName: String) : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue =
            requireAccessToken(request, environment.cipher) { accessToken ->
                val isEligible = environment.service.isEligible(accessToken, userName, electionName)
                responseBuilder().json(isEligible).build()
            }
    }


    data class Unsupported(val commandName: String, val content: String) : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue {
            return responseBuilder().unsupported("Unsupported command '$commandName'\n$content").build()
        }
    }

    data class Malformed(val commandName: String, val content: String) : ServiceCommand {
        override fun exec(environment: ServiceEnvironment, request: RequestValue): ResponseValue {
            return responseBuilder().unsupported("Malformed body for command '$commandName'\n$content").build()
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
            environment.topLevelException(exception)
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
                newElectionName,
                secretBallot,
                clearNoVotingBefore,
                noVotingBefore,
                clearNoVotingAfter,
                noVotingAfter
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
            val tokenResponse = mapOf(
                "accessToken" to accessTokenString,
                "userName" to tokens.accessToken.userName,
                "role" to tokens.accessToken.role.name
            )
            return responseBuilder().refreshToken(refreshTokenString).json(tokenResponse).build()
        }

        private fun responseBuilder(): ResponseBuilder = ResponseBuilder()

        private val statusCodeMap = mapOf(
            UNAUTHORIZED to 401,
            NOT_FOUND to 404,
            CONFLICT to 409,
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
