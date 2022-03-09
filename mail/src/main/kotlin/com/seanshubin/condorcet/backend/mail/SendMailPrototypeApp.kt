package com.seanshubin.condorcet.backend.mail

import javax.mail.Message
import javax.mail.Session
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


object SendMailPrototypeApp {
    @JvmStatic
    fun main(args: Array<String>) {
        val props = System.getProperties()
        props["mail.transport.protocol"] = "smtp"
        props["mail.smtp.port"] = 587
        props["mail.smtp.starttls.enable"] = "true"
        props["mail.smtp.auth"] = "true"
        val session = Session.getDefaultInstance(props)
        val msg = MimeMessage(session)
        msg.setFrom(InternetAddress("alice@pairwisevote.com", "Alice"))
        msg.setRecipient(Message.RecipientType.TO, InternetAddress("seanshubin@gmail.com"))
        msg.subject = "Amazon SES test (SMTP interface accessed using Java)"
        msg.setContent(
            java.lang.String.join(
                System.getProperty("line.separator"),
                "<h1>Amazon SES SMTP Email Test</h1>",
                "<p>This email was sent with Amazon SES using the ",
                "<a href='https://github.com/javaee/javamail'>Javamail Package</a>",
                " for <a href='https://www.java.com'>Java</a>."
            ), "text/html"
        )
        val transport = session.transport
        try {
            transport.connect(
                "email-smtp.us-east-1.amazonaws.com",
                "AKIA6BGHPRKQM5GUHDUZ",
                "BDLuVNBgwJqxws1RrIuUedoBKRzZx2F3Bcn+V00/gIdB"
            )
            transport.sendMessage(msg, msg.allRecipients)
        } finally {
            transport.close()
        }
    }
}
