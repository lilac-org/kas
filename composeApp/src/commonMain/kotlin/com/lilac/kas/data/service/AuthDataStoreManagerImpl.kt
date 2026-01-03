package com.lilac.kas.data.service

import com.lilac.kas.domain.model.UserDetail
import com.lilac.kas.domain.model.UserProfile
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

    override suspend fun saveUser(user: UserDetail) {
        println("Saving user")
        settings.putString("id", user.id)
        settings.putString("email", user.email)
        settings.putString("username", user.username)
        settings.putString("firstName", user.firstName)
        settings.putString("lastName", user.lastName)
        settings.putBoolean("isEmailVerified", user.isEmailVerified)
        settings.putLong("createdAt", user.createdAt)
        settings.putLong("updatedAt", user.updatedAt)
        user.profile?.bio?.let { settings.putString("bio", it) }
        user.profile?.profilePictureUrl?.let { settings.putString("profilePictureUrl", it) }
        user.profile?.coverPictureUrl?.let { settings.putString("coverPictureUrl", it) }
        user.profile?.updatedAt?.let { settings.putLong("profileUpdatedAt", it) }
    }

    override suspend fun saveAuth(accessToken: String, refreshToken: String, user: UserDetail) {
        println("Saving auth")
        saveTokens(accessToken, refreshToken)
        saveUser(user)
    }

    override val user: UserDetail?
        get() = getKasUser()

    override fun getKasUser(): UserDetail? {
        val id = settings.getStringOrNull("id") ?: return null

        val profile = UserProfile(
            bio = settings.getStringOrNull("bio"),
            profilePictureUrl = settings.getStringOrNull("profilePictureUrl"),
            coverPictureUrl = settings.getStringOrNull("coverPictureUrl"),
            updatedAt = settings.getLongOrNull("profileUpdatedAt") ?: 0
        )

        return UserDetail(
            id = id,
            email = settings.getString("email", ""),
            username = settings.getString("username", ""),
            firstName = settings.getString("firstName", ""),
            lastName = settings.getString("lastName", ""),
            createdAt = settings.getLong("createdAt", 0),
            updatedAt = settings.getLong("updatedAt", 0),
            isEmailVerified = settings.getBoolean("isEmailVerified", false),
            profile = profile
        )
    }

    override fun getTokens(): Pair<String?, String?> {
        return settings.getStringOrNull("accessToken") to settings.getStringOrNull("refreshToken")
    }

    override suspend fun clearDataStore() {
        settings.clear()
    }
}