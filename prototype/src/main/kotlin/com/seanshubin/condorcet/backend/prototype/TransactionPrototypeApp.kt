package com.seanshubin.condorcet.backend.prototype

import java.sql.DriverManager

object TransactionPrototypeApp {
    @JvmStatic
    fun main(args: Array<String>) {
        val host = "localhost"
        val user = "root"
        val password = "insecure"
        val schemaName = "transaction_prototype"
        val url = "jdbc:mysql://$host?serverTimezone=UTC"
        val prototypeUrl = "jdbc:mysql://$host/$schemaName?serverTimezone=UTC"
        val connectionA = DriverManager.getConnection(url, user, password)
        val dropDatabaseStatement =
            """drop database if exists $schemaName"""
        val createDatabaseStatement =
            """create database $schemaName"""
        val useDatabaseStatement =
            """use $schemaName"""
        val createTableStatement =
            """create table transaction_test (
                | id int not null auto_increment,
                | name varchar(255) not null unique,
                | primary key(id)
                |)""".trimMargin()
        val createInsertStatement =
            """insert into transaction_test (name) values (?)"""
        val listValuesStatement =
            """select * from transaction_test"""
        connectionA.prepareStatement(dropDatabaseStatement).execute()
        connectionA.prepareStatement(createDatabaseStatement).execute()
        connectionA.prepareStatement(useDatabaseStatement).execute()
        connectionA.prepareStatement(createTableStatement).execute()
        connectionA.close()
        for (c in 'A'..'Z') {
            val connectionB = DriverManager.getConnection(prototypeUrl, user, password)
            val statement = connectionB.prepareStatement(createInsertStatement)
            statement.setString(1, c.toString())
            statement.execute()
            connectionB.close()
        }
        val connectionC = DriverManager.getConnection(prototypeUrl, user, password)
        val resultSet = connectionC.prepareStatement(listValuesStatement).executeQuery()
        while (resultSet.next()) {
            val id = resultSet.getInt(1)
            val name = resultSet.getString(2)
            println("$id - $name")
        }
        connectionC.close()
    }
}
