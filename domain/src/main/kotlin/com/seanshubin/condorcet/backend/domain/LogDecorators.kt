package com.seanshubin.condorcet.backend.domain

import com.seanshubin.condorcet.backend.logger.Logger

object LogDecorators {
    fun logSql(logger: Logger): (String) -> Unit = { logger.log("${it.trim()};") }
}
