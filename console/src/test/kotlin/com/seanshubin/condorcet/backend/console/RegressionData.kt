package com.seanshubin.condorcet.backend.console

import com.seanshubin.condorcet.backend.server.NotificationsFactory
import java.nio.file.Path

interface RegressionData : NotificationsFactory {
    fun loadText(regressionFile: RegressionFile): String
    fun namePath(regressionFile: RegressionFile): Path
    fun fullPath(regressionFile: RegressionFile): Path
}
