package com.seanshubin.condorcet.backend.console

import com.seanshubin.condorcet.backend.dependencies.Dependencies
import java.sql.DriverManager

object ServerApp {
    @JvmStatic
    fun main(args: Array<String>) {
        if (args.isNotEmpty()) {
            debugSqlConnection(args)
        } else {
            Dependencies(ConsoleIntegration()).runner.run()
        }
    }

    private fun debugSqlConnection(args: Array<String>) {
        val host = args[0]
        val user = args[1]
        val password = args[2]
        val url = String.format("jdbc:mysql://%s?serverTimezone=UTC", host)
        val command = "show databases"
        Class.forName("com.mysql.cj.jdbc.Driver")
        DriverManager.getConnection(url, user, password).use { connection ->
            connection.createStatement().use { statement ->
                statement.executeQuery(command).use { resultSet ->
                    println(url)
                    println()
                    println(command)
                    println()
                    while (resultSet.next()) {
                        println(resultSet.getString(1))
                    }
                }
            }
        }
    }
}
