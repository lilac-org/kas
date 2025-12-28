package com.lilac.kas.response

import com.lilac.kas.dto.TokenPairDto
import kotlinx.serialization.Serializable

@Serializable
data class TokenPairResponse(
    val data: TokenPairDto,
    val success: Boolean = true,
    val message: String? = "Authentication successful"
)