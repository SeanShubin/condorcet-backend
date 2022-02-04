package com.seanshubin.condorcet.backend.genericdb

import com.seanshubin.condorcet.backend.contract.FilesContract
import java.nio.file.Path
import java.nio.file.StandardOpenOption

class SecretsOnFileSystem(
    private val secretsDir: Path,
    private val files: FilesContract
):Secrets {
    override fun databasePassword(): String =databaseValue("password")
    override fun databaseHost(): String = databaseValue("host")

    private fun databaseValue(name:String): String {
        val path = secretsDir.resolve("database-$name.txt")
        files.createDirectories(secretsDir)
        if(!files.exists(path)){
            files.writeString(path, "$name-goes-here", StandardOpenOption.CREATE)
        }
        return files.readString(path)
    }
}
