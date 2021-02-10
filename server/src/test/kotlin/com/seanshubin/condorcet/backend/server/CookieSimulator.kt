package com.seanshubin.condorcet.backend.server

import jakarta.servlet.http.Cookie

class CookieSimulator() {
    val cookieList: MutableList<Cookie> = mutableListOf()
    val addCookieInvocations: MutableList<Cookie> = mutableListOf()
    fun addCookie(cookie: Cookie) {
        addCookieInvocations.add(cookie)
        val existingCookie = cookieList.withIndex().find { (_, existingCookie) ->
            existingCookie.name.equals(cookie.name, ignoreCase = true)
        }
        if (existingCookie != null) {
            cookieList.removeAt(existingCookie.index)
        }
        cookieList.add(cookie)
    }

    fun getCookies(): Array<Cookie>? =
        if (cookieList.isEmpty()) null
        else cookieList.toTypedArray()
}
