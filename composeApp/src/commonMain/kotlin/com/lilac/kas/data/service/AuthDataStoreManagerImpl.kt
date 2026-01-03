package com.lilac.kas.data.service

import com.lilac.kas.domain.model.User
import com.lilac.kas.domain.service.AuthDataStoreManager
import com.russhwolf.settings.Settings

class AuthDataStoreManagerImpl(
    private val settings: Settings
): AuthDataStoreManager {
    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        println("Saving tokens")
        settings.putString("accessToken", accessToken)
        settings.putString("refreshToken", refreshToken)
    }

    override suspend fun saveUser(user: User) {
        println("Saving user")
        settings.putString("id", user.id)
        settings.putString("email", user.email)
        settings.putString("username", user.username)
        settings.putString("firstName", user.firstName)
        settings.putString("lastName", user.lastName)
        settings.putString("profileImageUrl", user.profileImageUrl ?: "")
        settings.putLong("createdAt", user.createdAt)
        settings.putLong("updatedAt", user.updatedAt)
    }

    override suspend fun saveAuth(accessToken: String, refreshToken: String, user: User) {
        println("Saving auth")
        saveTokens(accessToken, refreshToken)
        saveUser(user)
    }

    override val user: User?
        get() = getKasUser()

    override fun getKasUser(): User? {
        val id = settings.getStringOrNull("id") ?: return null

        return User(
            id = id,
            email = settings.getString("email", ""),
            username = settings.getString("username", ""),
            firstName = settings.getString("firstName", ""),
            lastName = settings.getString("lastName", ""),
            profileImageUrl = settings.getStringOrNull("profileImageUrl"),
            createdAt = settings.getLong("createdAt", 0),
            updatedAt = settings.getLong("updatedAt", 0)
        )
    }

    override fun getTokens(): Pair<String?, String?> {
        return settings.getStringOrNull("accessToken") to settings.getStringOrNull("refreshToken")
    }

    override suspend fun clearDataStore() {
        settings.clear()
    }
}