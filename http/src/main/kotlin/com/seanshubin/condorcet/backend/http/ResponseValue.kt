package com.seanshubin.condorcet.backend.http

data class ResponseValue(
    val status: Int,
    val body: String?,
    val headers: HeaderList
) {
    fun toLines(): List<String> {
        val statusLine = status.toString()
        val bodyLines = if (body == null) emptyList() else listOf(body)
        return listOf(statusLine) + headers.toLines() + bodyLines
    }
}