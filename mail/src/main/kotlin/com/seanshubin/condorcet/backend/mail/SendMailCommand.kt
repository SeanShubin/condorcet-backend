package com.seanshubin.condorcet.backend.mail

data class SendMailCommand(
    val fromName: String,
    val subject: String,
    val body: String,
    val toAddress: String,
    val toPersonal: String
)
