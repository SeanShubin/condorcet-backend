package com.seanshubin.condorcet.backend.server

class DispatchRunner(
    private val args:Array<String>,
    private val serverRunner: Runnable,
    private val backupRunner: Runnable,
    private val restoreRunner:Runnable
) : Runnable {
    private val commandMap = mapOf(
        "server" to serverRunner,
        "backup" to backupRunner,
        "restore" to restoreRunner
    )
    override fun run() {
        val commandName = args.getOrNull(0) ?: "server"
        val command = commandMap[commandName]
        if(command == null){
            println("Unsupported command '$commandName'")
        } else {
            command.run()
        }
    }
}
