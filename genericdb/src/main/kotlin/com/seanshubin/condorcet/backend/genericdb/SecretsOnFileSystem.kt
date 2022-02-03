package com.seanshubin.condorcet.backend.genericdb

import com.seanshubin.condorcet.backend.contract.FilesContract
import java.nio.file.Path

class SecretsOnFileSystem(
    private val secretsDir: Path,
    private val files: FilesContract
):Secrets {
    override fun databasePassword(): String {
        val path = secretsDir.resolve("database-password.txt")
        return files.readString(path)
    }
}
