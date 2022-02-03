package com.seanshubin.condorcet.backend.genericdb

import com.seanshubin.condorcet.backend.contract.FilesContract
import java.nio.file.OpenOption
import java.nio.file.Path
import java.nio.file.StandardOpenOption

class SecretsOnFileSystem(
    private val secretsDir: Path,
    private val files: FilesContract
):Secrets {
    override fun databasePassword(): String {
        val path = secretsDir.resolve("database-password.txt")
        files.createDirectories(secretsDir)
        if(!files.exists(path)){
           files.writeString(path, "password-goes-here", StandardOpenOption.CREATE)
        }
        return files.readString(path)
    }
}
