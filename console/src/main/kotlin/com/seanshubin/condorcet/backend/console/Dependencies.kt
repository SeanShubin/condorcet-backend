package com.seanshubin.condorcet.backend.console

import com.seanshubin.condorcet.backend.crypto.*
import com.seanshubin.condorcet.backend.database.Initializer
import com.seanshubin.condorcet.backend.database.SchemaInitializer
import com.seanshubin.condorcet.backend.database.util.ConnectionLifecycle
import com.seanshubin.condorcet.backend.database.util.ConnectionWrapper
import com.seanshubin.condorcet.backend.database.util.Lifecycle
import com.seanshubin.condorcet.backend.domain.*
import com.seanshubin.condorcet.backend.logger.LogGroup
import com.seanshubin.condorcet.backend.logger.Logger
import com.seanshubin.condorcet.backend.logger.LoggerFactory
import com.seanshubin.condorcet.backend.server.ApiHandler
import com.seanshubin.condorcet.backend.server.JettyServer
import com.seanshubin.condorcet.backend.server.Runner
import com.seanshubin.condorcet.backend.server.ServerContract
import org.eclipse.jetty.server.Handler
import org.eclipse.jetty.server.Server
import java.nio.file.Path
import java.nio.file.Paths

class Dependencies {
    private val logDir: Path = Paths.get("out", "log")
    private val logGroup: LogGroup = LoggerFactory.instanceDefaultZone.createLogGroup(logDir)
    private val sqlLogger: Logger = logGroup.create("sql")
    private val port: Int = 8080
    private val server: Server = Server(port)
    private val serverContract: ServerContract = JettyServer(server)
    private val serviceEventParser: ServiceEventParser = ApiServiceEventParser()
    private val uniqueIdGennerator: UniqueIdGenerator = Uuid4()
    private val oneWayHash: OneWayHash = Sha256Hash()
    private val passwordUtil: PasswordUtil = PasswordUtil(uniqueIdGennerator, oneWayHash)
    private val service: Service = ApiService(passwordUtil)
    private val handler: Handler = ApiHandler(serviceEventParser, service)
    private val host: String = "localhost"
    private val user: String = "root"
    private val password: String = "insecure"
    private val schemaName: String = "condorcet"
    private val sqlEvent: (String) -> Unit = LogDecorators.logSql(sqlLogger)
    private val connectionLifecycle: Lifecycle<ConnectionWrapper> =
        ConnectionLifecycle(host, user, password, sqlEvent)
    private val lifecycles: Lifecycles = DomainLifecycles(connectionLifecycle)
    private val initializer: Initializer = SchemaInitializer(lifecycles::connection, schemaName)
    val runner: Runnable = Runner(lifecycles, initializer, serverContract, handler)
}
