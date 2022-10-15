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
            configurationFactory.stringAt("localhost", listOf("database", "root", "host")).load
        override val lookupUser: () -> String =
            configurationFactory.stringAt("root", listOf("database", "root", "user")).load
        override val lookupPassword: () -> String =
            secretsConfigurationFactory.stringAt("database-password", listOf("database", "root", "password")).load
        override val lookupPort: () -> Int =
            configurationFactory.intAt(3306, listOf("database", "root", "port")).load
        override val lookupSchemaName: () -> String =
            configurationFactory.stringAt("root", listOf("database", "root", "name")).load
    }
    override val immutableDatabase: DatabaseConfiguration = object : DatabaseConfiguration {
        override val lookupHost: () -> String =
            configurationFactory.stringAt("localhost", listOf("database", "immutable", "host")).load
        override val lookupUser: () -> String =
            configurationFactory.stringAt("root", listOf("database", "immutable", "user")).load
        override val lookupPassword: () -> String = secretsConfigurationFactory.stringAt(
            "database-password",
            listOf("database", "immutable", "password")
        ).load
        override val lookupPort: () -> Int =
            configurationFactory.intAt(3306, listOf("database", "immutable", "port")).load
        override val lookupSchemaName: () -> String =
            configurationFactory.stringAt("immutable", listOf("database", "immutable", "name")).load
    }
    override val mutableDatabase: DatabaseConfiguration = object : DatabaseConfiguration {
        override val lookupHost: () -> String =
            configurationFactory.stringAt("localhost", listOf("database", "mutable", "host")).load
        override val lookupUser: () -> String =
            configurationFactory.stringAt("root", listOf("database", "mutable", "user")).load
        override val lookupPassword: () -> String = secretsConfigurationFactory.stringAt(
            "database-password",
            listOf("database", "mutable", "password")
        ).load
        override val lookupPort: () -> Int =
            configurationFactory.intAt(3306, listOf("database", "mutable", "port")).load
        override val lookupSchemaName: () -> String =
            configurationFactory.stringAt("mutable", listOf("database", "mutable", "name")).load
    }
    override val mail: MailConfiguration = object : MailConfiguration {
        override val lookupHost: () -> String =
            configurationFactory.stringAt("email-host", listOf("mail", "host")).load
        override val lookupUser: () -> String =
            configurationFactory.stringAt("email-user", listOf("mail", "user")).load
        override val lookupPassword: () -> String =
            secretsConfigurationFactory.stringAt("email-password", listOf("mail", "password")).load
        override val lookupFromDomain: () -> String =
            configurationFactory.stringAt("from-domain", listOf("mail", "fromDomain")).load
    }
    override val server: ServerConfiguration = object : ServerConfiguration {
        override val lookupPort: () -> Int = configurationFactory.intAt(8080, listOf("server", "port")).load
    }
}
