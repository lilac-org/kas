package com.lilac.kas.presentation.routes

import com.lilac.kas.config.AppConstant.JWT_NAME
import com.lilac.kas.config.AuthConfig
import com.lilac.kas.request.LoginRequest
import com.lilac.kas.request.RefreshTokenRequest
import com.lilac.kas.request.RegisterRequest
import com.lilac.kas.response.ErrorResponse
import com.lilac.kas.response.TokenPairResponse
import com.lilac.kas.response.UserDetailResponse
import com.lilac.kas.response.UserPublicDetailResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import org.koin.ktor.ext.inject

@OptIn(ExperimentalSerializationApi::class)
fun Route.identityRoutes() {
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

            call.respondIfSuccess<TokenPairResponse>(response)
        }

        post("/login") {
            val payload = call.receive<LoginRequest>()
            val response = identityClient.post("${authConfig.url}/api/auth/login") {
                setBody(payload)
            }

            call.respondIfSuccess<TokenPairResponse>(response)
        }

        post("/refresh") {
            val payload = call.receive<RefreshTokenRequest>()
            val response = identityClient.post("${authConfig.url}/api/auth/refresh") {
                setBody(payload)
            }

            call.respondIfSuccess<TokenPairResponse>(response)
        }
    }

    route("/users") {
        authenticate(JWT_NAME) {
            get("/me") {
                val authorizationHeader = call.request.headers["Authorization"]!!

                val response = identityClient.get("${authConfig.url}/api/users/me") {
                    header("Authorization", authorizationHeader)
                }

                call.respondIfSuccess<UserDetailResponse>(response)
            }
        }

        get("/{userId}") {
            val userId = call.parameters["userId"]

            val response = identityClient.get("${authConfig.url}/api/users/$userId")

            call.respondIfSuccess<UserPublicDetailResponse>(response)
        }
    }
}

private suspend inline fun <reified T: Any> ApplicationCall.respondIfSuccess(
    response: HttpResponse
) {
    if(response.status.isSuccess()) {
        this.respond(
            status = response.status,
            message = response.body<T>()
        )
    } else {
        this.respond(
            status = response.status,
            message = response.body<ErrorResponse>()
        )
    }
}