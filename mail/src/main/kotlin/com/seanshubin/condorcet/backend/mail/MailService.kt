package com.seanshubin.condorcet.backend.mail

interface MailService {
    fun sendMail(sendMailCommand: SendMailCommand)
}
