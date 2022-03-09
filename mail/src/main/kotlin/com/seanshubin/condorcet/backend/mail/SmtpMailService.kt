package com.seanshubin.condorcet.backend.mail

import javax.mail.Message
import javax.mail.Session
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class SmtpMailService(private val mailConfiguration: MailConfiguration):MailService {
    override fun sendMail(fromName: String, subject: String, body: String, toAddress: String, toPersonal: String) {
        val mailProperties = System.getProperties()
        mailProperties["mail.transport.protocol"] = "smtp"
        mailProperties["mail.smtp.port"] = 587
        mailProperties["mail.smtp.starttls.enable"] = "true"
        mailProperties["mail.smtp.auth"] = "true"
        val session = Session.getDefaultInstance(mailProperties)
        val mimeMessage = MimeMessage(session)
        val fromAddress = mailConfiguration.lookupFromAddress()
        mimeMessage.setFrom(InternetAddress(fromAddress))
        mimeMessage.setRecipient(Message.RecipientType.TO, InternetAddress(toAddress, toPersonal))
        mimeMessage.subject = subject
        mimeMessage.setContent(body, "text/plain")
        val host = mailConfiguration.lookupHost()
        val user = mailConfiguration.lookupUser()
        val password = mailConfiguration.lookupPassword()
        session.transport.use {
            it.connect(host,user,password)
            it.sendMessage(mimeMessage, mimeMessage.allRecipients)
        }
    }
}
