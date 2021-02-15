package com.seanshubin.condorcet.backend.console

import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.module.kotlin.readValue
import com.seanshubin.condorcet.backend.json.JsonMappers

class FakeBrowser {
    var cookieList: List<Pair<String, String>> = emptyList()
    var accessTokenString: String? = null
    fun requestHeaders(): List<Pair<String, String>> {
        return maybeAuthorizationHeader() + maybeCookieHeader()
    }

    fun maybeCookieHeader(): List<Pair<String, String>> {
        return if (cookieList.isEmpty()) {
            emptyList()
        } else {
            val name = "Cookie"
            val value = cookieList.map { (name, value) -> "$name=$value" }.joinToString("; ")
            return listOf(Pair(name, value))
        }
    }

    fun maybeAuthorizationHeader(): List<Pair<String, String>> {
        return if (accessTokenString == null) {
            emptyList()
        } else {
            val name = "Authorization"
            val value = "Bearer $accessTokenString"
            return listOf(Pair(name, value))
        }
    }

    fun handleResponse(response: ResponseStub) {
        val newAccessTokenString = checkForAccessToken(response)
        if (newAccessTokenString != null) {
            accessTokenString = newAccessTokenString
        }
        val setCookieHeaders = response.headers.filter { (name, _) ->
            name.equals("Set-Cookie", ignoreCase = true)
        }
        setCookieHeaders.map { (_, value) -> value }.forEach(::addCookie)
    }

    fun checkForAccessToken(response: ResponseStub): String? =
        try {
            val o = JsonMappers.parser.readValue<Any>(response.body)
            if (o is Map<*, *>) {
                o["accessToken"] as String?
            } else {
                null
            }
        } catch (ex: MismatchedInputException) {
            null
        }

    fun addCookie(headerValue: String) {
        val parts = headerValue.split("; ")
        val nameValue = parts[0]
        val nameValueParts = nameValue.split("=")
        val name = nameValueParts[0]
        val value = nameValueParts[1]
        addOrReplaceCookie(name, value)
    }

    fun addOrReplaceCookie(name: String, value: String) {
        val existingIndex = findIndex(name)
        cookieList = if (existingIndex == -1) {
            cookieList + Pair(name, value)
        } else {
            cookieList.mapIndexed { index, pair ->
                if (index == existingIndex) Pair(name, value)
                else pair
            }
        }
    }

    fun findIndex(name: String): Int =
        cookieList.withIndex().find { it.value.first == name }?.index ?: -1
}