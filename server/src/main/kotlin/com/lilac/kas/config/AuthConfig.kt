package com.lilac.kas.config

import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.Application
import java.nio.file.Files
import java.nio.file.Paths
import java.security.KeyFactory
import java.security.interfaces.RSAPublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

data class AuthConfig(
    val url: String,
    val issuer: String,
    val clientId: String,
    val realm: String,
    private val publicKeyPath: String
) {
    val algorithm: Algorithm = Algorithm.RSA256(
        loadPublicKey(publicKeyPath),
        null
    )
}

fun Application.loadAuthConfig(): AuthConfig {
    val cfg = environment.config

    return AuthConfig(
        url = cfg.propertyOrNull("auth.url")?.getString() ?: error("Auth Backend URL must be specified"),
        clientId = cfg.propertyOrNull("auth.clientId")?.getString() ?: error("Client Id must be specified"),
        issuer = cfg.propertyOrNull("auth.issuer")?.getString() ?: error("JWT Issuer must be specified"),
        realm = cfg.propertyOrNull("auth.realm")?.getString() ?: error("JWT Realm must be specified"),
        publicKeyPath = cfg.propertyOrNull("auth.publicKeyPath")?.getString() ?: error("JWT Public Key Path must be specified"),
    )
}

fun loadPublicKey(path: String): RSAPublicKey {
    val key = Files.readString(Paths.get(path))
        .replace("-----BEGIN PUBLIC KEY-----", "")
        .replace("-----END PUBLIC KEY-----", "")
        .replace("\\s".toRegex(), "")

    val decoded = Base64.getDecoder().decode(key)
    val spec = X509EncodedKeySpec(decoded)
    return KeyFactory.getInstance("RSA").generatePublic(spec) as RSAPublicKey
}