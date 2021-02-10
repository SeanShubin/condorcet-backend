package com.seanshubin.condorcet.backend.http

data class SetCookie(val name: String, val value: String, val httpOnly: Boolean = false, val secure: Boolean = false) {
    fun toHeader(): Header {
        val httpOnlyString = if (httpOnly) "; HttpOnly" else ""
        val secureString = if (httpOnly) "; Secure" else ""
        val cookieValue = "$name=$value$httpOnlyString$secureString"
        return Header("Set-Cookie", cookieValue)
    }

    companion object {
        fun parse(s: String): SetCookie {
            val parts = s.split("=")
            val name = parts[0]
            val value = parts[1]
            return SetCookie(name, value)
        }
    }
}
