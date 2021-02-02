package com.seanshubin.condorcet.backend.genericdb

data class DbEnum(val name: String, val table: Table, val valueNames: List<String>) {

    companion object {
        inline fun <reified T : Enum<T>> fromEnum(): DbEnum {
            val field = Field("name", FieldType.STRING)
            val name = T::class.simpleName!!.toLowerCase()
            val table = Table(T::class.simpleName!!.toLowerCase(), field)
            val valueNames = enumValues<T>().map { it.name }
            return DbEnum(name, table, valueNames)
        }
    }
}
