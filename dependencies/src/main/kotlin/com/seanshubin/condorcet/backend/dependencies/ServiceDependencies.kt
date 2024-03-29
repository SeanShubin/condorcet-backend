package com.seanshubin.condorcet.backend.dependencies

import com.seanshubin.condorcet.backend.crypto.OneWayHash
import com.seanshubin.condorcet.backend.crypto.PasswordUtil
import com.seanshubin.condorcet.backend.crypto.Sha256Hash
import com.seanshubin.condorcet.backend.crypto.UniqueIdGenerator
import com.seanshubin.condorcet.backend.database.*
import com.seanshubin.condorcet.backend.genericdb.*
import com.seanshubin.condorcet.backend.mail.MailService
import com.seanshubin.condorcet.backend.service.AccessToken
import com.seanshubin.condorcet.backend.service.BaseService
import com.seanshubin.condorcet.backend.service.RecordingService
import com.seanshubin.condorcet.backend.service.Service
import com.seanshubin.condorcet.backend.string.util.ByteArrayFormat
import com.seanshubin.condorcet.backend.string.util.ByteArrayFormatServiceLocator
import java.nio.charset.Charset
import java.time.Clock
import java.time.Duration

class ServiceDependencies(
    integration: Integration,
    eventConnection: ConnectionWrapper,
    stateConnection: ConnectionWrapper,
    mailService: MailService,
    charset: Charset,
    emailAccessTokenExpire: Duration,
    createUpdatePasswordLink: (AccessToken, String) -> String
) {
    private val serviceRequestEvent: (String, String) -> Unit = integration.serviceRequestEvent
    private val serviceResponseEvent: (String, String, String) -> Unit = integration.serviceResponseEvent
    private val queryLoader: QueryLoader = QueryLoaderFromResource()
    private val byteArrayFormat: ByteArrayFormat = ByteArrayFormatServiceLocator.byteArrayFormat
    private val oneWayHash: OneWayHash = Sha256Hash
    private val uniqueIdGenerator: UniqueIdGenerator = integration.uniqueIdGenerator
    private val passwordUtil: PasswordUtil = PasswordUtil(uniqueIdGenerator, oneWayHash, charset)
    private val random = integration.random
    private val eventGenericDatabase: GenericDatabase = GenericDatabaseImpl(
        eventConnection,
        queryLoader
    )
    val immutableDbQueries: ImmutableDbQueries = ImmutableDbQueriesImpl(
        eventGenericDatabase
    )
    private val stateGenericDatabase: GenericDatabase = GenericDatabaseImpl(
        stateConnection,
        queryLoader
    )
    private val clock: Clock = integration.clock
    private val mutableDbCommands: MutableDbCommands = MutableDbCommandsImpl(stateGenericDatabase)
    private val eventCommandParser: EventCommandParser = EventCommandParserImpl()
    private val mutableDbQueries: MutableDbQueries = MutableDbQueriesImpl(stateGenericDatabase)
    private val synchronizer: Synchronizer = SynchronizerImpl(
        immutableDbQueries,
        mutableDbQueries,
        mutableDbCommands,
        eventCommandParser
    )
    val immutableDbCommands: ImmutableDbCommands = ImmutableDbCommandsImpl(
        eventGenericDatabase,
        synchronizer
    )
    private val syncDbCommands: MutableDbCommands = SyncCommands(immutableDbCommands, clock)
    private val baseService: Service = BaseService(
        passwordUtil,
        immutableDbQueries,
        mutableDbQueries,
        syncDbCommands,
        synchronizer,
        random,
        clock,
        uniqueIdGenerator,
        mailService,
        emailAccessTokenExpire,
        createUpdatePasswordLink,
        byteArrayFormat
    )
    val service: Service = RecordingService(baseService, serviceRequestEvent, serviceResponseEvent)
}