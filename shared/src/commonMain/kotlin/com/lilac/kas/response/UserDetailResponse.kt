package com.lilac.kas.response

import com.lilac.kas.dto.UserDetailDto
import kotlinx.serialization.Serializable

@Serializable
data class UserDetailResponse(
    val data: UserDetailDto,
    val success: Boolean,
    val message: String?
)
