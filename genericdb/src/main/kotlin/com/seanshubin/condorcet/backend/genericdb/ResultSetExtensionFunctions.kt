package com.seanshubin.condorcet.backend.genericdb

import java.sql.ResultSet

object ResultSetExtensionFunctions {
    fun ResultSet.getIntOrNull(columnLabel:String):Int? {
        val x = getInt(columnLabel)
        return if(wasNull()) null else x
    }
}
