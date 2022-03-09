package com.seanshubin.condorcet.backend.mail

interface MailConfiguration {
    val lookupHost:() -> String
    val lookupUser:() -> String
    val lookupPassword:() -> String
    val lookupFromAddress:()->String
    fun reify(){
        lookupHost()
        lookupUser()
        lookupPassword()
        lookupFromAddress()
    }
}
