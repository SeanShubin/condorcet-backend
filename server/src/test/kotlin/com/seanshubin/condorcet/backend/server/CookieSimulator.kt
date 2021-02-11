package com.seanshubin.condorcet.backend.server

import com.seanshubin.condorcet.backend.http.CookieList
import com.seanshubin.condorcet.backend.http.HeaderList

class CookieSimulator() {
    var cookieList: CookieList = CookieList.empty
    fun cookieHeader():Pair<String, String> = cookieList.toHeader().toPair()
    fun trackCookies(headers:List<Pair<String, String>>){
        val setCookieList = HeaderList.fromPairs(headers).setCookieList()
        setCookieList.forEach{
            cookieList= cookieList.addCookie(it)
        }
    }
}
