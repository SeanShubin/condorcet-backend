package com.seanshubin.condorcet.backend.jwt

import com.auth0.jwt.algorithms.Algorithm
import com.seanshubin.condorcet.backend.contract.FilesContract
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec

class AlgorithmFactoryImpl(
    private val files: FilesContract,
    private val charset: Charset,
    private val keyBasePath: Path
) : AlgorithmFactory {
    private val publicKeyPath: Path = keyBasePath.resolve("public-key.txt")
    private val privateKeyPath: Path = keyBasePath.resolve("private-key.txt")
    override fun create(): Algorithm {
        val (publicKeyBytes, privateKeyBytes) = loadKeyBytes()
        val keyFactory = KeyFactory.getInstance("RSA")
        val publicKey: RSAPublicKey = keyFactory.generatePublic(X509EncodedKeySpec(publicKeyBytes)) as RSAPublicKey
        val privateKey: RSAPrivateKey =
            keyFactory.generatePrivate(PKCS8EncodedKeySpec(privateKeyBytes)) as RSAPrivateKey
        return Algorithm.RSA256(publicKey, privateKey)
    }

    private fun loadKeyBytes(): Pair<ByteArray, ByteArray> {
        return if (files.exists(publicKeyPath) && files.exists(privateKeyPath)) {
            loadExistingKeyBytes()
        } else {
            createAndLoadKeyBytes()
        }
    }

    private fun loadExistingKeyBytes(): Pair<ByteArray, ByteArray> {
        val publicKeyHex = files.readString(publicKeyPath)
        val publicKeyBytes = HexFormatter.hexToBytes(publicKeyHex)
        val privateKeyHex = files.readString(privateKeyPath)
        val privateKeyBytes = HexFormatter.hexToBytes(privateKeyHex)
        return Pair(publicKeyBytes, privateKeyBytes)
    }

    private fun createAndLoadKeyBytes(): Pair<ByteArray, ByteArray> {
        files.createDirectories(keyBasePath)
        val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
        keyPairGenerator.initialize(1024)
        val keyPair = keyPairGenerator.generateKeyPair()
        val publicKey: RSAPublicKey = keyPair.public as RSAPublicKey
        val privateKey: RSAPrivateKey = keyPair.private as RSAPrivateKey
        val publicKeyBytes: ByteArray = publicKey.encoded
        val privateKeyBytes: ByteArray = privateKey.encoded
        val publicKeyHex = HexFormatter.Pretty.bytesToHex(publicKeyBytes)
        val privateKeyHex = HexFormatter.Pretty.bytesToHex(privateKeyBytes)
        Files.writeString(publicKeyPath, publicKeyHex, charset)
        Files.writeString(privateKeyPath, privateKeyHex, charset)
        return loadExistingKeyBytes()
    }
}