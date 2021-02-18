package com.seanshubin.condorcet.backend.prototype

import java.sql.Connection
import java.sql.DriverManager

object TransactionPrototypeApp {
    data class IdName(val id: Int, val name: String) {
        override fun toString(): String = "$id - $name"
    }

    interface Api {
        fun addName(name: String)
        fun list(): List<IdName>
    }

    interface ApiLifecycle {
        fun <T> withApi(f: (Api) -> T): T
    }

    interface ConnectionLifecycle {
        fun <T> withConnection(f: (Connection) -> T): T
    }

    interface MyDatabase {
        fun addName(name: String)
        fun list(): List<IdName>
    }

    interface Initializer {
        fun initialize()
    }

    class InitializerImpl(
        private val connectionLifecycle: ConnectionLifecycle,
        private val schemaName: String
    ) : Initializer {
        override fun initialize() {
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
            connectionLifecycle.withConnection { connection ->
                connection.prepareStatement(dropDatabaseStatement).execute()
                connection.prepareStatement(createDatabaseStatement).execute()
                connection.prepareStatement(useDatabaseStatement).execute()
                connection.prepareStatement(createTableStatement).execute()
            }
        }
    }

    class MyDatabaseImpl(private val connection: Connection) : MyDatabase {
        override fun addName(name: String) {
            val createInsertStatement =
                """insert into transaction_test (name) values (?)"""
            val statement = connection.prepareStatement(createInsertStatement)
            statement.setString(1, name)
            statement.execute()
        }

        override fun list(): List<IdName> {
            val listValuesStatement =
                """select * from transaction_test"""
            val resultSet = connection.prepareStatement(listValuesStatement).executeQuery()
            val results = mutableListOf<IdName>()
            while (resultSet.next()) {
                val id = resultSet.getInt(1)
                val name = resultSet.getString(2)
                results.add(IdName(id, name))
            }
            return results
        }
    }

    class ApiImpl(private val myDatabase: MyDatabase) : Api {
        override fun addName(name: String) {
            myDatabase.addName(name)
        }

        override fun list(): List<IdName> {
            return myDatabase.list()
        }
    }

    class ConnectionLifecycleImpl(
        private val host: String,
        private val user: String,
        private val password: String
    ) : ConnectionLifecycle {
        override fun <T> withConnection(f: (Connection) -> T): T {
            val url = "jdbc:mysql://$host?serverTimezone=UTC"
            return DriverManager.getConnection(url, user, password).use { connection ->
                f(connection)
            }
        }
    }

    class TransactionConnectionLifecycle(
        private val host: String,
        private val user: String,
        private val password: String,
        private val schemaName: String
    ) : ConnectionLifecycle {
        override fun <T> withConnection(f: (Connection) -> T): T {
            val url = "jdbc:mysql://$host/$schemaName?serverTimezone=UTC"
            DriverManager.getConnection(url, user, password).use { connection ->
                connection.autoCommit = false
                val result = f(connection)
                connection.commit()
                return result
            }
        }
    }

    class ConnectionApiLifecycle(
        private val createApi: (Connection) -> Api,
        private val connectionLifecycle: ConnectionLifecycle
    ) : ApiLifecycle {
        override fun <T> withApi(f: (Api) -> T): T =
            connectionLifecycle.withConnection { connection ->
                val api: Api = createApi(connection)
                f(api)
            }
    }

    class ApiDelegateToLifecycle(private val apiLifecycle: ApiLifecycle) : Api {
        override fun addName(name: String) {
            apiLifecycle.withApi { api -> api.addName(name) }
        }

        override fun list(): List<IdName> =
            apiLifecycle.withApi { api -> api.list() }
    }

    class ConnectionDependencies(connection: Connection) {
        private val myDatabase: MyDatabase = MyDatabaseImpl(connection)
        val api: Api = ApiImpl(myDatabase)
    }

    class Runner(
        private val initializer: Initializer,
        private val api: Api,
        private val emitLine: (String) -> Unit
    ) : Runnable {
        override fun run() {
            initializer.initialize()
            for (c in 'A'..'Z') {
                api.addName(c.toString())
            }
            api.list().map { it.toString() }.forEach(emitLine)
        }
    }

    class Dependencies {
        private val createApi: (Connection) -> Api = { connection ->
            ConnectionDependencies(connection).api
        }
        private val host = "localhost"
        private val user = "root"
        private val password = "insecure"
        private val schemaName = "transaction_prototype"
        private val connectionLifecycle: ConnectionLifecycle = ConnectionLifecycleImpl(host, user, password)
        private val transactionConnectionLifecycle: ConnectionLifecycle =
            TransactionConnectionLifecycle(host, user, password, schemaName)
        private val apiLifecycle: ApiLifecycle = ConnectionApiLifecycle(createApi, transactionConnectionLifecycle)
        private val initializer: Initializer = InitializerImpl(connectionLifecycle, schemaName)
        private val api: Api = ApiDelegateToLifecycle(apiLifecycle)
        private val emitLine: (String) -> Unit = ::println
        val runner: Runnable = Runner(initializer, api, emitLine)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        Dependencies().runner.run()
    }
}
