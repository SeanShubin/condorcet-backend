package com.seanshubin.condorcet.backend.server

import java.nio.file.Path

interface NotificationsFactory {
    fun createNotifications(logDir: Path): Notifications
}
