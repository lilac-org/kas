package com.lilac.kas.domain.service

import com.lilac.kas.domain.model.User

interface AuthDataStoreManager {
    suspend fun saveTokens(accessToken: String, refreshToken: String)
    suspend fun saveUser(user: User)
    suspend fun saveAuth(
        accessToken: String,
        refreshToken: String,
        user: User
    )
    val user: User?
    fun getKasUser(): User?
    fun getTokens(): Pair<String?, String?>
    suspend fun clearDataStore()
}