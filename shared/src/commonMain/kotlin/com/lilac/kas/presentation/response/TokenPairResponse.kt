package com.lilac.kas.presentation.response

import com.lilac.kas.presentation.dto.TokenPairDto
import kotlinx.serialization.Serializable

@Serializable
data class TokenPairResponse(
    val data: TokenPairDto,
    val success: Boolean,
    val message: String?
)