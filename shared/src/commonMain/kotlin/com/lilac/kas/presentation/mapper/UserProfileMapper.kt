package com.lilac.kas.presentation.mapper

import com.lilac.kas.domain.model.UserProfile
import com.lilac.kas.presentation.dto.UserProfileDto

fun UserProfileDto.toDomain() = UserProfile(
    bio = this.bio,
    profilePictureUrl = this.profilePictureUrl,
    coverPictureUrl = this.coverPictureUrl,
    updatedAt = this.updatedAt
)