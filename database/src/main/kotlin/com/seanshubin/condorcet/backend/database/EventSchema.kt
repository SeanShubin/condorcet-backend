package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.genericdb.Field
import com.seanshubin.condorcet.backend.genericdb.FieldType.*
import com.seanshubin.condorcet.backend.genericdb.Schema
import com.seanshubin.condorcet.backend.genericdb.Table

object EventSchema : Schema {
    val eventWhen = Field("when", DATE)
    val eventAuthority = Field("authority", STRING)
    val eventType = Field("type", STRING)
    val eventText = Field("text", TEXT)
    val event = Table("event", eventWhen, eventAuthority, eventType, eventText)
    override val tables = listOf(
        event
    )
    override val initializeQueryName: String? = null
}
