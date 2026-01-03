package com.lilac.kas.domain.model

data class UserProfile(
    val bio: String?,
    val profilePictureUrl: String?,
    val coverPictureUrl: String?,
    val updatedAt: Long
)