package com.seanshubin.condorcet.backend.http

data class Header(val name: String, val value: String){
    fun toPair():Pair<String, String> = Pair(name, value)
    companion object{
        fun fromPair(pair:Pair<String, String>):Header =
            Header(pair.first, pair.second)
    }
}
