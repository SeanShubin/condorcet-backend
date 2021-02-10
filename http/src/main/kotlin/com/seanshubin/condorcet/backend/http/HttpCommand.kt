package com.seanshubin.condorcet.backend.http

interface HttpCommand {
    fun exec(request: RequestValue): ResponseValue
}
