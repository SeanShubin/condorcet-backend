package com.seanshubin.condorcet.backend.mail

interface MailConfiguration {
    val lookupHost: () -> String
    val lookupUser: () -> String
    val lookupPassword: () -> String
    val lookupFromDomain: () -> String
    fun reify() {
        lookupHost()
        lookupUser()
        lookupPassword()
        lookupFromDomain()
    }
}
