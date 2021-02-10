package com.seanshubin.condorcet.backend.prototype

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.eclipse.jetty.alpn.server.ALPNServerConnectionFactory
import org.eclipse.jetty.http2.server.HTTP2ServerConnectionFactory
import org.eclipse.jetty.server.*
import org.eclipse.jetty.server.handler.AbstractHandler
import org.eclipse.jetty.util.ssl.SslContextFactory

/*
edit /private/etc/hosts
add line
    127.0.0.1 condorcet.com

keytool -genkey -alias condorcet -keyalg RSA -keystore 'secrets/condorcet.jks' -dname "CN=*.condorcet.com, OU=Sean Shubin 4, O=Sean Shubin, L=Los Angeles, ST=California, C=US" -ext san=dns:condorcet.com -storepass insecure
keytool -export -alias condorcet -file 'secrets/condorcet.cer' -keystore 'secrets/condorcet.jks' -storepass insecure
keytool -importkeystore -srckeystore secrets/condorcet.jks -destkeystore secrets/condorcet.p12 -deststoretype PKCS12 -srcstorepass insecure -deststorepass insecure
openssl pkcs12 -in secrets/condorcet.p12 -nocerts -out secrets/condorcet.key -nodes -password pass:insecure
keytool -list -v -keystore 'secrets/condorcet.jks' -storepass insecure
https://condorcet.com:8443
 */

object PrototypeTlsServerApp {
    @JvmStatic
    fun main(args: Array<String>) {
        val server = Server()
        val httpConfig = HttpConfiguration()
        httpConfig.addCustomizer(SecureRequestCustomizer())
        val http11 = HttpConnectionFactory(httpConfig)
        val h2 = HTTP2ServerConnectionFactory(httpConfig)
        val alpn = ALPNServerConnectionFactory()
        alpn.defaultProtocol = http11.protocol
        val sslContextFactory = SslContextFactory.Server()
        sslContextFactory.keyStorePath = "secrets/condorcet.jks"
        sslContextFactory.setKeyStorePassword("insecure")
        val tls = SslConnectionFactory(sslContextFactory, alpn.protocol)
        val connector = ServerConnector(server, tls, alpn, http11, h2)
        connector.port = 8443
        server.addConnector(connector)
        server.handler = PrototypeHandler()
        server.start()
        server.join()
    }

    class PrototypeHandler : AbstractHandler() {
        override fun handle(
            target: String?,
            baseRequest: Request,
            request: HttpServletRequest?,
            response: HttpServletResponse
        ) {
            response.contentType = "text/plain"
            response.status = HttpServletResponse.SC_OK
            response.writer.println("Hello, world!")
            baseRequest.isHandled = true
        }
    }
}
