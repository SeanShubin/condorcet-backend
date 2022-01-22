package com.seanshubin.condorcet.backend.dependencies

import com.seanshubin.condorcet.backend.crypto.OneWayHash
import com.seanshubin.condorcet.backend.crypto.PasswordUtil
import com.seanshubin.condorcet.backend.crypto.Sha256Hash
import com.seanshubin.condorcet.backend.crypto.UniqueIdGenerator
import com.seanshubin.condorcet.backend.database.*
import com.seanshubin.condorcet.backend.genericdb.*
import com.seanshubin.condorcet.backend.service.Service
import com.seanshubin.condorcet.backend.service.BaseService
import com.seanshubin.condorcet.backend.service.RecordingService
import java.time.Clock

class ServiceDependencies(
    integration: Integration,
    eventConnection: ConnectionWrapper,
    stateConnection: ConnectionWrapper
) {
    private val serviceRequestEvent:(String)->Unit = integration.serviceRequestEvent
    private val serviceResponseEvent:(String, String)->Unit = integration.serviceResponseEvent
    private val queryLoader: QueryLoader = QueryLoaderFromResource()
    private val oneWayHash: OneWayHash = Sha256Hash()
    private val uniqueIdGenerator: UniqueIdGenerator = integration.uniqueIdGenerator
    private val passwordUtil: PasswordUtil = PasswordUtil(uniqueIdGenerator, oneWayHash)
    private val random = integration.random
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
    private val clock: Clock = integration.clock
    private val stateDbCommands: StateDbCommands = StateDbCommandsImpl(stateGenericDatabase)
    private val dbEventParser: DbEventParser = DbEventParserImpl()
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
    private val baseService: Service = BaseService(
        passwordUtil,
        eventDbQueries,
        stateDbQueries,
        syncDbCommands,
        synchronizer,
        random,
        clock,
        uniqueIdGenerator
    )
    val service:Service = RecordingService(baseService, serviceRequestEvent, serviceResponseEvent)
}