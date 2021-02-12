package com.seanshubin.condorcet.backend.http

data class SetCookie(val name: String, val value: String, val httpOnly: Boolean = false, val secure: Boolean = false) {
    fun toHeader(): Header {
        val httpOnlyString = if (httpOnly) "; HttpOnly" else ""
        val secureString = if (secure) "; Secure" else ""
        val cookieValue = "$name=$value$httpOnlyString$secureString"
        return Header("Set-Cookie", cookieValue)
    }

    fun toCookie():Cookie = Cookie(name, value)

    companion object {
        fun parse(s: String): SetCookie {
            val parts = s.split("; ")
            val nameValue = parts[0]
            val nameValueParts = nameValue.split("=")
            val name = nameValueParts[0]
            val value = nameValueParts[1]
            val attributes = parts.drop(1)
            val httpOnly = attributes.any{ it.equals("HttpOnly", ignoreCase = true)}
            val secure = attributes.any{ it.equals("Secure", ignoreCase = true)}
            return SetCookie(name, value, httpOnly, secure)
        }
    }
}
