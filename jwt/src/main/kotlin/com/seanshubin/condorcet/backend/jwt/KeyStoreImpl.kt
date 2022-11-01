package com.seanshubin.condorcet.backend.jwt

import com.seanshubin.condorcet.backend.contract.FilesContract
import com.seanshubin.condorcet.backend.string.util.ByteArrayFormat
import java.nio.charset.Charset
import java.nio.file.Path
import java.security.KeyPairGenerator

class KeyStoreImpl(
    private val files: FilesContract,
    private val charset: Charset,
    private val basePath: Path,
    private val byteArrayFormat: ByteArrayFormat
) : KeyStore {
    private val publicKeyPath: Path = basePath.resolve("rsa-public-key.txt")
    private val privateKeyPath: Path = basePath.resolve("rsa-private-key.txt")
    override fun privateKey(): ByteArray {
        createBothIfEitherMissing()
        return loadBytes(privateKeyPath)
    }

    override fun publicKey(): ByteArray {
        createBothIfEitherMissing()
        return loadBytes(publicKeyPath)
    }

    private fun createBothIfEitherMissing() {
        if (!files.exists(publicKeyPath) || !files.exists(privateKeyPath)) {
            createBoth()
        }
    }

    private fun loadBytes(path: Path): ByteArray {
        val encoded = files.readString(path)
        val bytes = byteArrayFormat.decode(encoded)
        return bytes
    }

    private fun createBoth() {
        files.createDirectories(basePath)
        val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
        keyPairGenerator.initialize(1024)
        val keyPair = keyPairGenerator.generateKeyPair()
        writeKey(publicKeyPath, keyPair.public.encoded)
        writeKey(privateKeyPath, keyPair.private.encoded)
    }

    private fun writeKey(path: Path, bytes: ByteArray) {
        val encodedPretty = byteArrayFormat.encodePretty(bytes)
        files.writeString(path, encodedPretty, charset)
    }
}