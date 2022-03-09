package com.seanshubin.condorcet.backend.mail

interface MailService {
    fun sendMail(fromName:String,
                 subject:String,
                 body:String,
                 toAddress:String,
                 toPersonal:String)
}
