package com.lilac.kas.data.mapper

import com.lilac.kas.domain.model.User
import com.lilac.kas.dto.UserDto

fun UserDto.toDomain(): User {
    return User(
        id = this.id,
        email = this.email,
        username = this.username,
        firstName = this.firstName,
        lastName = this.lastName,
        profileImageUrl = this.profileImageUrl,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt

    )
}