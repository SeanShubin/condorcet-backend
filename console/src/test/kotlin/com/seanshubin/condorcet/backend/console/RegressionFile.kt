package com.seanshubin.condorcet.backend.console

enum class RegressionFile(val fileName: String, val extension: String) {
    HTTP("http", "txt"),
    ROOT("root", "sql"),
    EVENT("event", "sql"),
    EVENT_TABLE("event-table", "txt"),
    STATE("state", "sql"),
    STATE_TABLE("state-table", "txt");
}
