package com.seanshubin.condorcet.backend.server

import com.seanshubin.condorcet.backend.genericdb.DatabaseConfiguration
import com.seanshubin.condorcet.backend.mail.MailConfiguration

interface Configuration {
    val rootDatabase: DatabaseConfiguration
    val immutableDatabase: DatabaseConfiguration
    val mutableDatabase: DatabaseConfiguration
    val mail: MailConfiguration
    val server: ServerConfiguration
    fun reify() {
        rootDatabase.reify()
        immutableDatabase.reify()
        mail.reify()
        server.reify()
    }
}