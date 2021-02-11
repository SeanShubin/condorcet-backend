package com.seanshubin.condorcet.backend.http

data class CookieList(val list: List<Cookie>) {
    fun addCookie(setCookie: SetCookie): CookieList {
        val targetIndex = list.indexOfFirst { it.name.equals(setCookie.name, ignoreCase = true) }
        return if (targetIndex == -1) {
            CookieList(list + setCookie.toCookie())
        } else {
            CookieList(list.mapIndexed { index, cookie ->
                if (index == targetIndex) {
                    setCookie.toCookie()
                } else {
                    cookie
                }
            })
        }
    }

    fun cookieValue(name: String): String? =
        list.find { it.name.equals(name, ignoreCase = true) }?.value

    fun toHeader(): Header {
        val name = "Cookie"
        val value = list.map { it.toString() }.joinToString("; ")
        return Header(name, value)
    }

    companion object {
        val empty = CookieList(emptyList())
    }
}
