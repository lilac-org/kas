package com.lilac.kas.domain.service

import com.lilac.kas.domain.model.UserDetail

interface AuthDataStoreManager {
    suspend fun saveTokens(accessToken: String, refreshToken: String)
    suspend fun saveUser(user: UserDetail)
    suspend fun saveAuth(
        accessToken: String,
        refreshToken: String,
        user: UserDetail
    )
    val user: UserDetail?
    fun getKasUser(): UserDetail?
    fun getTokens(): Pair<String?, String?>
    suspend fun clearDataStore()
}