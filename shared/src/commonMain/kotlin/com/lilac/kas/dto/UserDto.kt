package com.lilac.kas.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: String,
    val email: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    val profileImageUrl: String?,
    val createdAt: Long,
    val updatedAt: Long
)
