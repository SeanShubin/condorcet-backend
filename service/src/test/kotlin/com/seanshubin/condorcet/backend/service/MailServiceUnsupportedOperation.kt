package com.seanshubin.condorcet.backend.service

import com.seanshubin.condorcet.backend.mail.MailService
import com.seanshubin.condorcet.backend.mail.SendMailCommand

class MailServiceUnsupportedOperation : MailService {
    override fun sendMail(sendMailCommand: SendMailCommand) {
        throw UnsupportedOperationException("not implemented")
    }
}
