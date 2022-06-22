package com.seanshubin.condorcet.backend.database

class ImmutableDbOperations(
    val immutableDbQueries: ImmutableDbQueries,
    val immutableDbCommands: ImmutableDbCommands
) {
    operator fun component1(): ImmutableDbQueries = immutableDbQueries
    operator fun component2(): ImmutableDbCommands = immutableDbCommands
}
