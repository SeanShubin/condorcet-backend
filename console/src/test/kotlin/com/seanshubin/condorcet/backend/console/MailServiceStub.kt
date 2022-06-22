package com.seanshubin.condorcet.backend.console

import com.seanshubin.condorcet.backend.mail.MailService
import com.seanshubin.condorcet.backend.mail.SendMailCommand

class MailServiceStub(val sendMailEvent: (SendMailCommand) -> Unit) : MailService {
    override fun sendMail(sendMailCommand: SendMailCommand) {
        sendMailEvent(sendMailCommand)
    }
}
