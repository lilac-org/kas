package com.lilac.kas.domain.use_case

import com.lilac.kas.domain.repository.AuthRepository
import com.lilac.kas.domain.service.AuthDataStoreManager

class AuthUseCase(
    private val authRepository: AuthRepository,
    private val authDataStoreManager: AuthDataStoreManager
) {
    suspend fun login(
        identifier: String,
        password: String
    ): Result<Unit> = try {
        authRepository.login(
            identifier = identifier,
            password = password
        )
            .onSuccess { tokenPair ->
                authDataStoreManager.saveTokens(
                    accessToken = tokenPair.accessToken,
                    refreshToken = tokenPair.refreshToken
                )

                authRepository.getUserDetail()
                    .onSuccess { userDetail ->
                        authDataStoreManager.saveUser(
                            user = userDetail
                        )
                    }.onFailure {
                        authDataStoreManager.clearDataStore()
                    }
            }.map { }
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun register(
        email: String,
        username: String,
        password: String,
        firstName: String,
        lastName: String
    ): Result<Unit> = try {
        authRepository.register(
            email = email,
            username = username,
            password = password,
            firstName = firstName,
            lastName = lastName
        )
            .onSuccess { tokenPair ->
                authDataStoreManager.saveTokens(
                    accessToken = tokenPair.accessToken,
                    refreshToken = tokenPair.refreshToken
                )
            }
            .map { }
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun logout(): Result<Unit> = try {
        authRepository.logout()
            .onSuccess {
                authDataStoreManager.clearDataStore()
            }
            .map { }
    } catch (e: Exception) {
        Result.failure(e)
    }
}