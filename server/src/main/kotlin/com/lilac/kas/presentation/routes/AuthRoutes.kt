package com.lilac.kas.presentation.routes

import com.lilac.kas.config.AuthConfig
import com.lilac.kas.request.LoginRequest
import com.lilac.kas.request.RegisterRequest
import com.lilac.kas.response.ErrorResponse
import com.lilac.kas.response.TokenPairResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import org.koin.ktor.ext.inject

@OptIn(ExperimentalSerializationApi::class)
fun Route.authRoutes() {
    val authConfig by inject<AuthConfig>()

    val identityClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                    allowTrailingComma = true
                    namingStrategy = JsonNamingStrategy.SnakeCase
                }
            )
        }
        install(DefaultRequest) {
            contentType(ContentType.Application.Json)
            header("X-Client-Id", authConfig.clientId)
        }
    }

    route("/auth") {
        post("/register") {
            val payload = call.receive<RegisterRequest>()
            val response = identityClient.post("${authConfig.url}/api/auth/register") {
                setBody(payload)
            }

            if(response.status.isSuccess()) {
                call.respond(
                    response.status,
                    response.body<TokenPairResponse>()
                )
            } else {
                call.respond(
                    response.status,
                    response.body<ErrorResponse>()
                )
            }
        }

        post("/login") {
            val payload = call.receive<LoginRequest>()
            val response = identityClient.post("${authConfig.url}/api/auth/login") {
                setBody(payload)
            }

            if(response.status.isSuccess()) {
                call.respond(
                    response.status,
                    response.body<TokenPairResponse>()
                )
            } else {
                call.respond(
                    response.status,
                    response.body<ErrorResponse>()
                )
            }
        }
    }
}