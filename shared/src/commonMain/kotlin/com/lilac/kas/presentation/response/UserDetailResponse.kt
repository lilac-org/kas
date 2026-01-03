package com.lilac.kas.presentation.response

import com.lilac.kas.presentation.dto.UserDetailDto
import kotlinx.serialization.Serializable

@Serializable
data class UserDetailResponse(
    val data: UserDetailDto,
    val success: Boolean,
    val message: String?
)
