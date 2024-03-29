package com.seanshubin.condorcet.backend.http

data class HeaderList(val list: List<Header>) {
    fun headerValue(name: String): String? =
        list.find { it.name.equals(name, ignoreCase = true) }?.value

    fun cookieValue(name: String): String? = cookies().cookieValue(name)
    fun bearerToken(): String? {
        val authorizationHeaderValue = headerValue("Authorization") ?: return null
        val matchResult = bearerTokenPattern.matchEntire(authorizationHeaderValue) ?: return null
        return matchResult.groupValues[1]
    }

    fun filterByName(name: String): List<Header> =
        list.filter { it.name.equals(name, ignoreCase = true) }

    fun setCookieList(): List<SetCookie> =
        filterByName("Set-Cookie").map {
            SetCookie.parse(it.value)
        }

    fun cookies(): CookieList {
        val cookieValue = headerValue("Cookie") ?: return CookieList.empty
        val cookieStrings = cookieValue.split("; ")
        val list = cookieStrings.map(Cookie::parse)
        return CookieList(list)
    }

    fun toPairs(): List<Pair<String, String>> =
        list.map { header -> Pair(header.name, header.value) }

    fun toLines(): List<String> = list.map { it.toLine() }

    companion object {
        val bearerTokenPattern = Regex("""^ *Bearer +(.*) *$""", RegexOption.IGNORE_CASE)
        fun fromPairs(pairs: List<Pair<String, String>>): HeaderList =
            HeaderList(pairs.map { Header.fromPair(it) })
    }
}
