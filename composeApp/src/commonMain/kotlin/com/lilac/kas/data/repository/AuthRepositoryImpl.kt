package com.lilac.kas.data.repository

import com.lilac.kas.data.Constant.BACKEND_BASE_URL
import com.lilac.kas.domain.model.TokenPair
import com.lilac.kas.domain.model.UserDetail
import com.lilac.kas.domain.repository.AuthRepository
import com.lilac.kas.presentation.mapper.toDomain
import com.lilac.kas.presentation.request.LoginRequest
import com.lilac.kas.presentation.request.RegisterRequest
import com.lilac.kas.presentation.response.ErrorResponse
import com.lilac.kas.presentation.response.TokenPairResponse
import com.lilac.kas.presentation.response.UserDetailResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.isSuccess

class AuthRepositoryImpl(
    val httpClient: HttpClient
): AuthRepository {
    override suspend fun login(identifier: String, password: String): Result<TokenPair> = try {
        val response = httpClient.post("$BACKEND_BASE_URL/auth/login") {
            setBody(
                LoginRequest(
                    identifier = identifier,
                    password = password
                )
            )
        }

        if(response.status.isSuccess()) {
            val body = response.body<TokenPairResponse>()

            Result.success(
                TokenPair(
                    accessToken = body.data.accessToken,
                    refreshToken = body.data.refreshToken
                )
            )
        } else {
            val body = response.body<ErrorResponse>()

            Result.failure(
                Exception(body.message ?: "Login Failed")
            )
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun register(
        email: String,
        username: String,
        password: String,
        firstName: String,
        lastName: String
    ): Result<TokenPair> = try {
        val response = httpClient.post("$BACKEND_BASE_URL/auth/register") {
            setBody(
                RegisterRequest(
                    email = email,
                    username = username,
                    password = password,
                    firstName = firstName,
                    lastName = lastName
                )
            )
        }

        if(response.status.isSuccess()) {
            val body = response.body<TokenPairResponse>()
            Result.success(
                TokenPair(
                    accessToken = body.data.accessToken,
                    refreshToken = body.data.refreshToken
                )
            )
        } else {
            val body = response.body<ErrorResponse>()

            Result.failure(Exception(body.message ?: "Registration Failed"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun logout(): Result<Unit> {
        println("Logout and does nothing to backend")
        return Result.success(Unit)
    }

    override suspend fun getUserDetail(): Result<UserDetail> = try {
        val response = httpClient.get("$BACKEND_BASE_URL/users/me")

        if(response.status.isSuccess()) {
            val body = response.body<UserDetailResponse>()
            Result.success(body.data.toDomain())
        } else {
            val body = response.body<ErrorResponse>()

            Result.failure(Exception(body.message ?: "Failed to get user detail"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}