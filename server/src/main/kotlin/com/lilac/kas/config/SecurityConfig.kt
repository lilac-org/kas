package com.lilac.kas.config

import com.auth0.jwt.JWT
import com.lilac.kas.config.AppConstant.JWT_NAME
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import org.koin.ktor.ext.getKoin

fun Application.configureSecurity() {
    val jwt = getKoin().get<AuthConfig>()

    install(Authentication) {
        jwt(JWT_NAME) {
            realm = jwt.realm
            verifier(
                JWT
                    .require(jwt.algorithm)
                    .withAudience(jwt.clientId)
                    .withIssuer(jwt.issuer)
                    .build()
            )

            validate { credential ->
                val payload = credential.payload

                payload.subject?.takeIf { it.isNotBlank() } ?: return@validate null
                val type = payload.getClaim("type").asString()
                if (type != "Access") return@validate null

                JWTPrincipal(payload)
            }
        }
    }
}