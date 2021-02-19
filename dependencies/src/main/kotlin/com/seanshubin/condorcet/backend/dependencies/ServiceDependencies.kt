package com.seanshubin.condorcet.backend.dependencies

import com.seanshubin.condorcet.backend.crypto.OneWayHash
import com.seanshubin.condorcet.backend.crypto.PasswordUtil
import com.seanshubin.condorcet.backend.crypto.Sha256Hash
import com.seanshubin.condorcet.backend.crypto.UniqueIdGenerator
import com.seanshubin.condorcet.backend.database.*
import com.seanshubin.condorcet.backend.genericdb.*
import com.seanshubin.condorcet.backend.service.ApiService
import com.seanshubin.condorcet.backend.service.Service
import java.time.Clock

class ServiceDependencies(
    integration: Integration,
    eventConnection: ConnectionWrapper,
    stateConnection: ConnectionWrapper
) {
    private val queryLoader: QueryLoader = QueryLoaderFromResource()
    private val oneWayHash: OneWayHash = Sha256Hash()
    private val uniqueIdGenerator: UniqueIdGenerator = integration.uniqueIdGenerator
    private val passwordUtil: PasswordUtil = PasswordUtil(uniqueIdGenerator, oneWayHash)
    private val eventGenericDatabase: GenericDatabase = GenericDatabaseImpl(
        eventConnection,
        queryLoader
    )
    private val eventDbQueries: EventDbQueries = EventDbQueriesImpl(
        eventGenericDatabase
    )
    private val stateGenericDatabase: GenericDatabase = GenericDatabaseImpl(
        stateConnection,
        queryLoader
    )
    private val stateDbCommands: StateDbCommands = StateDbCommandsImpl(stateGenericDatabase)
    private val dbEventParser: DbEventParser = DbEventParserImpl()
    private val clock: Clock = integration.clock
    private val stateDbQueries: StateDbQueries = StateDbQueriesImpl(stateGenericDatabase)
    private val synchronizer: Synchronizer = SynchronizerImpl(
        eventDbQueries,
        stateDbQueries,
        stateDbCommands,
        dbEventParser
    )
    private val eventDbCommands: EventDbCommands = EventDbCommandsImpl(
        eventGenericDatabase,
        synchronizer,
        clock
    )
    private val syncDbCommands: StateDbCommands = SyncDbCommands(eventDbCommands)
    val service: Service = ApiService(
        passwordUtil,
        syncDbCommands,
        stateDbQueries,
        stateConnection,
        eventConnection
    )
}