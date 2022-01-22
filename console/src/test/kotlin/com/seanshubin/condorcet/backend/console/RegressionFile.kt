package com.seanshubin.condorcet.backend.console

enum class RegressionFile(val fileName: String, val extension: String) {
    HTTP("http", "txt"),
    SERVICE("service", "txt"),
    ROOT("root", "sql"),
    EVENT("event", "sql"),
    EVENT_TABLE("event-table", "txt"),
    STATE("state", "sql"),
    STATE_TABLE("state-table", "txt"),
    TOP_LEVEL_EXCEPTION("top-level-exception", "txt");
}
