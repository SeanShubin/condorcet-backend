package com.seanshubin.condorcet.backend.prototype

import java.nio.file.Files
import java.nio.file.Paths
import java.security.Key
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec

object LoadStoreKeyApp {
    val baseDir = Paths.get("out", "prototype")

    @JvmStatic
    fun main(args: Array<String>) {
        Files.createDirectories(baseDir)
        val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
        keyPairGenerator.initialize(1024)
        val keyPair = keyPairGenerator.generateKeyPair()

        val publicKey: RSAPublicKey = keyPair.public as RSAPublicKey
        val privateKey: RSAPrivateKey = keyPair.private as RSAPrivateKey

        val publicKeyBytes: ByteArray = publicKey.encoded
        val privateKeyBytes: ByteArray = privateKey.encoded

        writeKey("public-key-1", publicKey)
        writeKey("private-key-1", privateKey)

        val keyFactory = KeyFactory.getInstance("RSA")
        val privateKey2: RSAPrivateKey =
            keyFactory.generatePrivate(PKCS8EncodedKeySpec(privateKeyBytes)) as RSAPrivateKey
        val publicKey2: RSAPublicKey = keyFactory.generatePublic(X509EncodedKeySpec(publicKeyBytes)) as RSAPublicKey

        writeKey("public-key-2", publicKey2)
        writeKey("private-key-2", privateKey2)
    }

    fun writeKey(fileName: String, key: Key) {
        val file = baseDir.resolve("$fileName.txt")
        val bytes = key.encoded
        val hex = HexFormatter.Pretty.bytesToHex(bytes)
        Files.writeString(file, hex)
    }
}
