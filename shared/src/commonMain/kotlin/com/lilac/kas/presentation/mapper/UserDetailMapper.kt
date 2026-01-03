package com.lilac.kas.presentation.mapper

import com.lilac.kas.domain.model.UserDetail
import com.lilac.kas.presentation.dto.UserDetailDto

fun UserDetailDto.toDomain() = UserDetail(
    id = this.id,
    email = this.email,
    username = this.username,
    firstName = this.firstName,
    lastName = this.lastName,
    isEmailVerified = this.isEmailVerified,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
    profile = this.profile?.toDomain()
)