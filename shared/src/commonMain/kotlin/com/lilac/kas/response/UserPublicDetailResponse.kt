package com.lilac.kas.response

import com.lilac.kas.dto.UserPublicDetailDto
import kotlinx.serialization.Serializable

@Serializable
data class UserPublicDetailResponse(
    val data: UserPublicDetailDto,
    val success: Boolean,
    val message: String?
)