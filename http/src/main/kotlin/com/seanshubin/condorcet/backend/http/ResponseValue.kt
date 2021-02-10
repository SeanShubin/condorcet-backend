package com.seanshubin.condorcet.backend.http

data class ResponseValue(
    val status: Int,
    val body: String?,
    val headers: HeaderList
)
