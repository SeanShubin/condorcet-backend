package com.seanshubin.condorcet.backend.genericdb

interface GenericTableViewer {
    fun tableNames(): List<String>
    fun tableData(name: String): GenericTable
}
