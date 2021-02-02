package com.seanshubin.condorcet.backend.genericdb

import com.seanshubin.condorcet.backend.genericdb.FieldType.*

object EventSchema : Schema {
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
