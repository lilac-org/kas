package com.lilac.kas.data.service

import com.lilac.kas.data.Constant.BACKEND_BASE_URL
import com.lilac.kas.domain.model.TokenPair
import com.lilac.kas.domain.service.AuthDataStoreManager
import com.lilac.kas.presentation.request.RefreshTokenRequest
import com.lilac.kas.presentation.response.TokenPairResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.plugins.plugin
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy

object HttpClientFactory {
    @OptIn(ExperimentalSerializationApi::class)
    fun create(
        engine: HttpClientEngine,
        dataStore: AuthDataStoreManager
    ): HttpClient {
        println("Creating http client: $engine")
        val client = HttpClient(engine) {
            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.ALL
            }

            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                        encodeDefaults = true
                        namingStrategy = JsonNamingStrategy.SnakeCase
                    }
                )
            }
            defaultRequest {
                contentType(ContentType.Application.Json)
            }
        }

        client.plugin(HttpSend.Plugin).intercept { request ->
            val tokens = dataStore.getTokens()
            val accessToken = tokens.first

            request.headers.remove(HttpHeaders.Authorization)

            if(!accessToken.isNullOrBlank()) {
                request.headers.append(
                    HttpHeaders.Authorization,
                    "Bearer $accessToken"
                )
            }

            val originalCall = execute(request)

            if(originalCall.response.status == HttpStatusCode.Unauthorized) {
                val refreshToken = tokens.second
                if(!refreshToken.isNullOrBlank()) {
                    try {
                        val newTokens = refreshTokenFromServer(refreshToken, engine)
                        dataStore.saveTokens(newTokens.accessToken, newTokens.refreshToken)

                        request.headers.remove(HttpHeaders.Authorization)
                        request.headers.append(
                            HttpHeaders.Authorization,
                            "Bearer ${newTokens.accessToken}"
                        )

                        return@intercept execute(request)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        dataStore.clearDataStore()
                        return@intercept originalCall
                    }
                }
            }

            originalCall
        }
        return client
    }

    @OptIn(ExperimentalSerializationApi::class)
    private suspend fun refreshTokenFromServer(
        refreshToken: String,
        engine: HttpClientEngine
    ): TokenPair {
        val refreshClient = HttpClient(engine) {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                        encodeDefaults = true
                        namingStrategy = JsonNamingStrategy.SnakeCase
                    }
                )
            }
        }

        try {
            val response = refreshClient.post("$BACKEND_BASE_URL/auth/refresh") {
                contentType(ContentType.Application.Json)
                setBody(RefreshTokenRequest(refreshToken))
            }

            val body = response.body<TokenPairResponse>()
            return TokenPair(
                accessToken = body.data.accessToken,
                refreshToken = body.data.refreshToken
            )
        } finally {
            refreshClient.close()
        }
    }
}