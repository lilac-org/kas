package com.lilac.kas.presentation.response

import com.lilac.kas.presentation.dto.UserPublicDetailDto
import kotlinx.serialization.Serializable

@Serializable
data class UserPublicDetailResponse(
    val data: UserPublicDetailDto,
    val success: Boolean,
    val message: String?
)