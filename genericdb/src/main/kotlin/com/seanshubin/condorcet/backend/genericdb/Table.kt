package com.seanshubin.condorcet.backend.genericdb

data class Table(
    val name: String,
    val columns: List<Column>,
    val unique: List<Column>
) {
    constructor(name: String, vararg columns: Column) : this(name, columns.toList(), listOf())

    fun toDropTableStatement(): String = "drop table if exists $name"

    fun toCreateTableStatements(): List<String> {
        val createTableSql = toCreateTableSql()
        val uniqueLines = toUniqueSql()
        return listOf<String>() + createTableSql + uniqueLines
    }

    fun toSelectAllStatement(): String = "select * from $name"

    private fun toCreateTableSql(): String {
        val firstLine = "create table $name ("
        val middleLines = toMiddleSql().map(::indent)
        val lastLine = ")"
        return (emptyList<String>() + firstLine + middleLines + lastLine).joinToString("\n")
    }

    private fun toMiddleSql(): List<String> {
        val idLine = "id int not null auto_increment,"
        val fieldLines: List<String> = this.columns.flatMap { it.toSql() }
        val primaryKeyLine = "primary key(id)"
        return listOf<String>() + idLine + fieldLines + primaryKeyLine
    }

    private fun toUniqueSql(): List<String> =
        if (unique.isEmpty()) emptyList()
        else {
            val uniqueColumnNames = unique.map { it.sqlName() }.joinToString(", ")
            listOf("alter table $name add unique unique_$name($uniqueColumnNames)")
        }

    private fun indent(s: String): String = "    $s"
}
