package com.seanshubin.condorcet.backend.service

import com.seanshubin.condorcet.backend.mail.MailService

class MailServiceUnsupportedOperation:MailService {
    override fun sendMail(fromName: String, subject: String, body: String, toAddress: String, toPersonal: String) {
        throw UnsupportedOperationException("not implemented")
    }
}
