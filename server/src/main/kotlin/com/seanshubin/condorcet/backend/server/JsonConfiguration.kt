package com.seanshubin.condorcet.backend.server

import com.seanshubin.condorcet.backend.configuration.util.ConfigurationFactory
import com.seanshubin.condorcet.backend.genericdb.DatabaseConfiguration
import com.seanshubin.condorcet.backend.mail.MailConfiguration

class JsonConfiguration(
    configurationFactory: ConfigurationFactory,
    secretsConfigurationFactory: ConfigurationFactory
) : Configuration {
    override val rootDatabase: DatabaseConfiguration = object : DatabaseConfiguration {
        override val lookupHost: () -> String =
            configurationFactory.createStringLookup("localhost", listOf("database", "root", "host"))
        override val lookupUser: () -> String =
            configurationFactory.createStringLookup("root", listOf("database", "root", "user"))
        override val lookupPassword: () -> String =
            secretsConfigurationFactory.createStringLookup("database-password", listOf("database", "root", "password"))
        override val lookupPort: () -> Int =
            configurationFactory.createIntLookup(3306, listOf("database", "root", "port"))
        override val lookupSchemaName: () -> String =
            configurationFactory.createStringLookup("root", listOf("database", "root", "name"))
    }
    override val immutableDatabase: DatabaseConfiguration = object : DatabaseConfiguration {
        override val lookupHost: () -> String =
            configurationFactory.createStringLookup("localhost", listOf("database", "immutable", "host"))
        override val lookupUser: () -> String =
            configurationFactory.createStringLookup("root", listOf("database", "immutable", "user"))
        override val lookupPassword: () -> String = secretsConfigurationFactory.createStringLookup(
            "database-password",
            listOf("database", "immutable", "password")
        )
        override val lookupPort: () -> Int =
            configurationFactory.createIntLookup(3306, listOf("database", "immutable", "port"))
        override val lookupSchemaName: () -> String =
            configurationFactory.createStringLookup("immutable", listOf("database", "immutable", "name"))
    }
    override val mutableDatabase: DatabaseConfiguration = object : DatabaseConfiguration {
        override val lookupHost: () -> String =
            configurationFactory.createStringLookup("localhost", listOf("database", "mutable", "host"))
        override val lookupUser: () -> String =
            configurationFactory.createStringLookup("root", listOf("database", "mutable", "user"))
        override val lookupPassword: () -> String = secretsConfigurationFactory.createStringLookup(
            "database-password",
            listOf("database", "mutable", "password")
        )
        override val lookupPort: () -> Int =
            configurationFactory.createIntLookup(3306, listOf("database", "mutable", "port"))
        override val lookupSchemaName: () -> String =
            configurationFactory.createStringLookup("mutable", listOf("database", "mutable", "name"))
    }
    override val mail: MailConfiguration = object : MailConfiguration {
        override val lookupHost: () -> String =
            configurationFactory.createStringLookup("email-host", listOf("mail", "host"))
        override val lookupUser: () -> String =
            configurationFactory.createStringLookup("email-user", listOf("mail", "user"))
        override val lookupPassword: () -> String =
            secretsConfigurationFactory.createStringLookup("email-password", listOf("mail", "password"))
        override val lookupFromAddress: () -> String =
            configurationFactory.createStringLookup("email-from-address", listOf("mail", "from"))
        override val lookupAppName: () -> String =
            configurationFactory.createStringLookup("app-name", listOf("mail", "applicationName"))
    }
    override val server: ServerConfiguration = object : ServerConfiguration {
        override val lookupPort: () -> Int = configurationFactory.createIntLookup(8080, listOf("server", "port"))
    }
}
