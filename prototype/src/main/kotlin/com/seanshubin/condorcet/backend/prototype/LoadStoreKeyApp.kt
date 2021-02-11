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
    val baseDir = Paths.get("prototype", "src", "main", "resources")

    @JvmStatic
    fun main(args: Array<String>) {
        Files.createDirectories(baseDir)
        val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
        keyPairGenerator.initialize(1024)
        val keyPair = keyPairGenerator.generateKeyPair()

        val publicKey: RSAPublicKey = keyPair.public as RSAPublicKey
        val privateKey: RSAPrivateKey = keyPair.private as RSAPrivateKey

        val publicKeyBytes: ByteArray = publicKey.encoded
        println(publicKeyBytes.size)
        val privateKeyBytes: ByteArray = privateKey.encoded
        println(privateKeyBytes.size)

        storeKey("public-key-1", publicKey)
        storeKey("private-key-1", privateKey)

        val keyFactory = KeyFactory.getInstance("RSA")
        val privateKey2: RSAPrivateKey =
            keyFactory.generatePrivate(PKCS8EncodedKeySpec(privateKeyBytes)) as RSAPrivateKey
        val publicKey2: RSAPublicKey = keyFactory.generatePublic(X509EncodedKeySpec(publicKeyBytes)) as RSAPublicKey

        storeKey("public-key-2", publicKey2)
        storeKey("private-key-2", privateKey2)

        val loadedPublicKeyBytes = loadBytes("public-key-1")
        val loadedPrivateKeyBytes = loadBytes("private-key-1")
        val privateKey3: RSAPrivateKey =
            keyFactory.generatePrivate(PKCS8EncodedKeySpec(loadedPrivateKeyBytes)) as RSAPrivateKey
        val publicKey3: RSAPublicKey = keyFactory.generatePublic(X509EncodedKeySpec(loadedPublicKeyBytes)) as RSAPublicKey

    }

    fun storeKey(fileName: String, key: Key) {
        val file = baseDir.resolve("$fileName.txt")
        val bytes = key.encoded
        val hex = HexFormatter.Pretty.bytesToHex(bytes)
        Files.writeString(file, hex)
    }
    fun loadBytes(fileName: String):ByteArray {
        val file = baseDir.resolve("$fileName.txt")
        val hex = Files.readString(file)
        val bytes = HexFormatter.hexToBytes(hex)
        return bytes
    }
}
