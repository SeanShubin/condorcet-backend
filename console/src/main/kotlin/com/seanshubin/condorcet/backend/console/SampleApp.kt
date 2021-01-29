package com.seanshubin.condorcet.backend.console

import com.seanshubin.condorcet.backend.domain.Command
import java.net.http.HttpClient

object SampleApp {
    @JvmStatic
    fun main(args: Array<String>) {
        val commands = listOf(
            Command.AddUser(name = "Alice", email = "alice@email.com", password = "alice-password"),
            Command.AddUser(name = "Bob", email = "bob@email.com", password = "bob-password"),
            Command.AddUser(name = "Carol", email = "carol@email.com", password = "carol-password"),
            Command.AddUser(name = "Dave", email = "dave@email.com", password = "dave-password"),
            Command.Authenticate(nameOrEmail = "Alice", password = "alice-password")
        )
        val client = HttpClient.newHttpClient()
        for (command in commands) {

        }
    }
}