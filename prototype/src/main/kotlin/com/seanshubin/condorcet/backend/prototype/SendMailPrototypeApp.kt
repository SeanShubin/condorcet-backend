package com.seanshubin.condorcet.backend.prototype

import com.seanshubin.condorcet.backend.configuration.util.ConfigurationFactory
import com.seanshubin.condorcet.backend.configuration.util.JsonFileConfigurationFactory
import com.seanshubin.condorcet.backend.contract.FilesContract
import com.seanshubin.condorcet.backend.contract.FilesDelegate
import java.nio.file.Paths
import java.util.*
import javax.mail.Message
import javax.mail.Session
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


object SendMailPrototypeApp {
    @JvmStatic
    fun main(args: Array<String>) {
        val files: FilesContract = FilesDelegate
        val configFilePath = Paths.get("local-config","secrets","send-mail-prototype-config.json")
        val configurationFactory: ConfigurationFactory = JsonFileConfigurationFactory(files, configFilePath)
        val host = configurationFactory.stringAt("email host", listOf("email", "host")).load()
        val user = configurationFactory.stringAt("email user", listOf("email", "user")).load()
        val password = configurationFactory.stringAt("email password", listOf("email", "password")).load()
        val counterConfig = configurationFactory.intAt(0, listOf("counter"))
        val counter = counterConfig.load() + 1
        counterConfig.store(counter)
        val mailProperties = Properties()
        mailProperties["mail.transport.protocol"] = "smtp"
        mailProperties["mail.smtp.port"] = 587
        mailProperties["mail.smtp.starttls.enable"] = "true"
        mailProperties["mail.smtp.auth"] = "true"
        val props = System.getProperties()
        mailProperties.forEach{ key, value ->
            props[key] = value
        }
        val session = Session.getDefaultInstance(props)
        val msg = MimeMessage(session)
//        msg.setFrom(InternetAddress("alice@pairwisevote.com", "Alice"))
        msg.setRecipient(Message.RecipientType.TO, InternetAddress("seanshubin@gmail.com"))
        msg.subject = "Amazon SES test $counter (SMTP interface accessed using Java)"
        msg.setContent(
            java.lang.String.join(
                System.getProperty("line.separator"),
                "<h1>Amazon SES SMTP Email Test $counter</h1>",
                "<p>This email was sent with Amazon SES using the ",
                "<a href='https://github.com/javaee/javamail'>Javamail Package</a>",
                " for <a href='https://www.java.com'>Java</a>."
            ), "text/html"
        )
        val transport = session.transport
        try {
            println("host = $host")
            println("user = $user")
            println("password = ${password[0]}<snip>${password[password.length-1]}")
            println("properties")
            mailProperties.map{"  $it"}.forEach(::println)
            transport.connect(
                host,
                user,
                password
            )
            transport.sendMessage(msg, msg.allRecipients)
        } finally {
            transport.close()
        }
    }
}
