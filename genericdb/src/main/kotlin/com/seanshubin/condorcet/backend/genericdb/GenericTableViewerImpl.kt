package com.seanshubin.condorcet.backend.genericdb

class GenericTableViewerImpl(
    private val schema: Schema,
    private val genericDatabase: GenericDatabase
) : GenericTableViewer, GenericDatabase by genericDatabase {
    override fun tableNames(): List<String> =
        schema.tables.map { it.name }

    override fun tableData(name: String): GenericTable {
        val table = schema.tables.find { it.name == name }!!
        return queryUntyped(table.toSelectAllStatement())
    }
}
