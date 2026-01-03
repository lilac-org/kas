package com.lilac.kas.domain.repository

import com.lilac.kas.domain.model.TokenPair
import com.lilac.kas.domain.model.UserDetail

interface AuthRepository {
    suspend fun login(identifier: String, password: String): Result<TokenPair>
    suspend fun register(
        email: String,
        username: String,
        password: String,
        firstName: String,
        lastName: String
    ): Result<TokenPair>
    suspend fun logout(): Result<Unit>

    suspend fun getUserDetail(): Result<UserDetail>
}