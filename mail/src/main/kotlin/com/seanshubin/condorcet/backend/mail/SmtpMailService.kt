package com.seanshubin.condorcet.backend.mail

import javax.mail.Message
import javax.mail.Session
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class SmtpMailService(
    private val mailConfiguration: MailConfiguration,
    val sendMailEvent: (SendMailCommand) -> Unit
) : MailService {
    override fun sendMail(sendMailCommand: SendMailCommand) {
        val fromName = sendMailCommand.fromName
        val toAddress = sendMailCommand.toAddress
        val toPersonal = sendMailCommand.toPersonal
        val subject = sendMailCommand.subject
        val body = sendMailCommand.body
        val mailProperties = System.getProperties()
        mailProperties["mail.transport.protocol"] = "smtp"
        mailProperties["mail.smtp.port"] = 587
        mailProperties["mail.smtp.starttls.enable"] = "true"
        mailProperties["mail.smtp.auth"] = "true"
        val session = Session.getDefaultInstance(mailProperties)
        val mimeMessage = MimeMessage(session)
        val fromDomain = mailConfiguration.lookupFromDomain()
        val fromAddress = "$fromName@$fromDomain"
        mimeMessage.setFrom(InternetAddress(fromAddress))
        mimeMessage.setRecipient(Message.RecipientType.TO, InternetAddress(toAddress, toPersonal))
        mimeMessage.subject = subject
        mimeMessage.setContent(body, "text/plain")
        val host = mailConfiguration.lookupHost()
        val user = mailConfiguration.lookupUser()
        val password = mailConfiguration.lookupPassword()
        session.transport.use {
            it.connect(host, user, password)
            it.sendMessage(mimeMessage, mimeMessage.allRecipients)
        }
        sendMailEvent(sendMailCommand)
    }
}
