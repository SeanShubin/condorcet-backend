package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.database.util.DbEnum
import com.seanshubin.condorcet.backend.database.util.Field
import com.seanshubin.condorcet.backend.database.util.FieldType.*
import com.seanshubin.condorcet.backend.database.util.Table

object CondorcetEventSchema : Schema {
    val eventWhen = Field("when", DATE)
    val eventType = Field("type", STRING)
    val eventText = Field("text", TEXT)
    val event = Table("event", eventWhen, eventType, eventText)
    val intVariableName = Field("name", STRING, unique = true)
    val intVariableValue = Field("value", INT)
    val intVariable = Table("int_variable", intVariableName, intVariableValue)
    override val name: String = "condorcet_event"
    override val tables = listOf(
        intVariable,
        event
    )
    override val enums: List<DbEnum> = emptyList()
}
